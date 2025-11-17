package com.smartspend.controller;

import com.smartspend.model.Expense;
import com.smartspend.model.User;
import com.smartspend.repository.ExpenseRepository;
import com.smartspend.repository.UserRepository;
import com.smartspend.service.EmailService;
import com.smartspend.service.ExpenseService;
import com.smartspend.service.BudgetService;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private EmailService emailService;

    // LIST EXPENSES WITH FILTERS
    @GetMapping
    public String listExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) String search,
            Model model,
            Authentication auth) {

        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        LocalDate start = (startDate != null && !startDate.isBlank()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isBlank()) ? LocalDate.parse(endDate) : null;

        List<Expense> expenses = expenseService.filterExpensesByUser(
                currentUser, category, type, start, end, minAmount, maxAmount, search
        );

        model.addAttribute("expenses", expenses);
        model.addAttribute("categories", expenseService.getFixedCategories());

        return "expenses";
    }

    // SHOW FORM
    @GetMapping({"/new", "/edit/{id}"})
    public String showForm(@PathVariable(required = false) Long id,
                           Model model,
                           Authentication auth) {

        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        Expense expense = (id != null)
                ? expenseRepository.findById(id).orElse(new Expense())
                : new Expense();

        model.addAttribute("expense", expense);
        model.addAttribute("categories", expenseService.getFixedCategories());
        budgetService.updateUsedAmounts(currentUser);

        return "expense_form";
    }

    // SAVE EXPENSE
    @PostMapping
    public String saveExpense(@ModelAttribute Expense expense, Authentication auth) {

        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        expense.setUser(currentUser);
        expenseRepository.save(expense);

        budgetService.updateUsedAmounts(currentUser);

        return "redirect:/expenses";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, Authentication auth) {

        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        Expense expense = expenseRepository.findById(id).orElseThrow();

        if (expense.getUser().getId().equals(currentUser.getId())) {
            expenseRepository.delete(expense);
        }
        budgetService.updateUsedAmounts(currentUser);

        return "redirect:/expenses";
    }

    // EXPORT EXCEL
    @GetMapping("/export/excel")
    public void exportExcel(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            Authentication auth,
            HttpServletResponse response) throws Exception {

        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        LocalDate start = (startDate != null && !startDate.isBlank()) ? LocalDate.parse(startDate) : null;
        LocalDate end   = (endDate != null && !endDate.isBlank()) ? LocalDate.parse(endDate) : null;

        List<Expense> expenses = expenseService.filterExpensesByUser(
                currentUser, category, type, start, end, null, null, search
        );

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expenses");

        String[] cols = {"ID", "Title", "Amount", "Category", "Type", "Date", "Description"};
        Row header = sheet.createRow(0);

        for (int i = 0; i < cols.length; i++) {
            header.createCell(i).setCellValue(cols[i]);
        }

        int rowIdx = 1;
        for (Expense e : expenses) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(e.getId());
            row.createCell(1).setCellValue(e.getTitle());
            row.createCell(2).setCellValue(e.getAmount());
            row.createCell(3).setCellValue(e.getCategory());
            row.createCell(4).setCellValue(e.getType());
            row.createCell(5).setCellValue(e.getDate() != null ? e.getDate().toString() : "");
            row.createCell(6).setCellValue(e.getDescription());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // EXPORT PDF
    @GetMapping("/export/pdf")
    public void exportPdf(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            Authentication auth,
            HttpServletResponse response) throws Exception {

        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        LocalDate start = (startDate != null && !startDate.isBlank()) ? LocalDate.parse(startDate) : null;
        LocalDate end   = (endDate != null && !endDate.isBlank()) ? LocalDate.parse(endDate) : null;

        List<Expense> expenses = expenseService.filterExpensesByUser(
                currentUser, category, type, start, end, null, null, search
        );

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        PdfPTable table = new PdfPTable(7);

        Stream.of("ID", "Title", "Amount", "Category", "Type", "Date", "Description")
                .forEach(h -> {
                    PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(cell);
                });

        for (Expense e : expenses) {
            table.addCell(String.valueOf(e.getId()));
            table.addCell(e.getTitle());
            table.addCell(String.valueOf(e.getAmount()));
            table.addCell(e.getCategory());
            table.addCell(e.getType());
            table.addCell(e.getDate() != null ? e.getDate().toString() : "");
            table.addCell(e.getDescription());
        }

        document.add(table);
        document.close();
    }
}

