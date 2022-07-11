package com.exampleproject.api.services;

import com.exampleproject.api.model.Author;
import com.exampleproject.api.model.LendingRegister;
import com.exampleproject.api.model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final AuthorService authorService;
    private final LendingRegisterService lendingRegisterService;

    public FileService(AuthorService authorService, LendingRegisterService lendingRegisterService) {
        this.authorService = authorService;
        this.lendingRegisterService = lendingRegisterService;
    }

    public boolean importAuthorsFromFile(Part file) throws IOException {

        InputStream excel = file.getInputStream();

        Author author;

        Workbook workbook = new XSSFWorkbook(excel);
        Sheet sheet = workbook.getSheet("Arkusz1");
        Iterator<Row> rows = sheet.iterator();

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            Iterator<Cell> cells = currentRow.cellIterator();
            author = new Author();

            while (cells.hasNext()) {
                Cell cell = cells.next();

                switch (cell.getColumnIndex()) {
                    case 0:
                        author.setFirstName(currentRow.getCell(0).getStringCellValue());
                        break;
                    case 1:
                        author.setSecondName(currentRow.getCell(1).getStringCellValue());
                        break;
                    case 2:
                        author.setLastName(currentRow.getCell(2).getStringCellValue());
                        break;
                    default:
                }
            }
            authorService.addAuthor(author);

        }

        workbook.close();

        return true;
    }

    public void getRegister(HttpServletResponse response, User user) throws IOException {

        Optional<List<LendingRegister>> optional = lendingRegisterService.getRegisterForUser(user);
        if (optional.isPresent()) {
            List<LendingRegister> lendingRegisterList = optional.get();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            Row row = sheet.createRow(0);
            int i = 1;

            Cell cell0 = row.createCell(0);
            Cell cell1 = row.createCell(1);
            Cell cell2 = row.createCell(2);
            Cell cell3 = row.createCell(3);
            Cell cell4 = row.createCell(4);

            cell0.setCellValue("LP");
            cell1.setCellValue("Tytuł książki");
            cell2.setCellValue("Właściciel");
            cell3.setCellValue("Data pożyczenia");
            cell4.setCellValue("Data oddania");

            for (LendingRegister lendingRegister : lendingRegisterList) {
                row = sheet.createRow(i);
                cell0 = row.createCell(0);
                cell1 = row.createCell(1);
                cell2 = row.createCell(2);
                cell3 = row.createCell(3);
                cell4 = row.createCell(4);

                cell0.setCellValue(i);
                cell1.setCellValue(lendingRegister.getBook().getTitle());
                cell2.setCellValue(lendingRegister.getBook().getOwner().getName());
                cell3.setCellValue(lendingRegister.getDateOfLend());
                cell4.setCellValue(lendingRegister.getDateOfReturn());
                i++;
            }


            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);

        }

    }
}