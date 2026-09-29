package com.hotel.ui;

import com.hotel.model.Payment;
import com.hotel.model.Reservation;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Generates an elegant, minimalist printable HTML invoice with zero external dependencies.
 */
public class InvoiceGenerator {
    private static final String INVOICES_DIR = "invoices";

    public static File generateInvoiceHtml(Reservation reservation, List<Payment> payments) {
        File dir = new File(INVOICES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "Invoice_" + reservation.getReservationId() + ".html");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        String payDetails = "Pending Payment";
        if (payments != null && !payments.isEmpty()) {
            Payment p = payments.get(payments.size() - 1);
            payDetails = "PAID via " + p.getMethod().getLabel() + " (Ref: " + p.getTransactionRef() + ")";
        }

        double subtotal = reservation.getTotalPrice();
        double tax = subtotal * 0.10; // 10% hospitality tax
        double grandTotal = subtotal + tax;

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>Invoice - " + reservation.getReservationId() + "</title>\n" +
                "  <style>\n" +
                "    body { font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, Roboto, sans-serif; margin: 0; padding: 40px; background: #f8fafc; color: #0f172a; }\n" +
                "    .invoice-card { max-width: 680px; margin: auto; background: #ffffff; border: 1px solid #e2e8f0; border-radius: 8px; padding: 36px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }\n" +
                "    .header { display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 1px solid #e2e8f0; padding-bottom: 24px; }\n" +
                "    .brand h1 { margin: 0; font-size: 22px; letter-spacing: 0.5px; color: #0f172a; }\n" +
                "    .brand p { margin: 4px 0 0; font-size: 13px; color: #64748b; }\n" +
                "    .inv-meta { text-align: right; }\n" +
                "    .inv-meta h2 { margin: 0; font-size: 18px; color: #0284c7; }\n" +
                "    .inv-meta p { margin: 4px 0 0; font-size: 13px; color: #64748b; }\n" +
                "    .guest-grid { display: flex; justify-content: space-between; margin: 24px 0; font-size: 13px; }\n" +
                "    .guest-box h3 { margin: 0 0 6px; font-size: 11px; text-transform: uppercase; letter-spacing: 0.5px; color: #64748b; }\n" +
                "    .table { width: 100%; border-collapse: collapse; margin: 20px 0; font-size: 13px; }\n" +
                "    .table th { text-align: left; padding: 10px 12px; background: #f8fafc; border-bottom: 1px solid #e2e8f0; color: #475569; font-weight: 600; }\n" +
                "    .table td { padding: 12px; border-bottom: 1px solid #f1f5f9; }\n" +
                "    .total-box { margin-left: auto; width: 260px; font-size: 13px; margin-top: 16px; }\n" +
                "    .total-row { display: flex; justify-content: space-between; padding: 6px 0; }\n" +
                "    .total-row.grand { border-top: 2px solid #0f172a; padding-top: 10px; font-size: 16px; font-weight: bold; color: #0f172a; }\n" +
                "    .status-badge { display: inline-block; padding: 4px 10px; border-radius: 4px; font-size: 12px; font-weight: 600; background: #ecfdf5; color: #059669; }\n" +
                "    .footer { margin-top: 36px; padding-top: 20px; border-top: 1px solid #e2e8f0; text-align: center; font-size: 12px; color: #94a3b8; }\n" +
                "    .print-btn { display: inline-block; background: #0f172a; color: white; border: none; padding: 8px 18px; border-radius: 6px; font-weight: 600; cursor: pointer; text-decoration: none; margin-bottom: 20px; }\n" +
                "    @media print { .print-btn { display: none; } body { padding: 0; background: white; } .invoice-card { box-shadow: none; border: none; padding: 0; } }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div style=\"max-width: 680px; margin: auto; text-align: right;\">\n" +
                "    <button class=\"print-btn\" onclick=\"window.print()\">Print / Save as PDF</button>\n" +
                "  </div>\n" +
                "  <div class=\"invoice-card\">\n" +
                "    <div class=\"header\">\n" +
                "      <div class=\"brand\">\n" +
                "        <h1>LUMINA HOTEL & RESIDENCES</h1>\n" +
                "        <p>100 Seaside Boulevard, Suite 400<br>Contact: reservations@luminahotel.com</p>\n" +
                "      </div>\n" +
                "      <div class=\"inv-meta\">\n" +
                "        <h2>INVOICE</h2>\n" +
                "        <p><strong>#" + reservation.getReservationId() + "</strong><br>Date: " + reservation.getBookingTime().format(dtf) + "</p>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <div class=\"guest-grid\">\n" +
                "      <div class=\"guest-box\">\n" +
                "        <h3>Billed To</h3>\n" +
                "        <strong>" + reservation.getGuest().getFullName() + "</strong><br>\n" +
                "        Phone: " + reservation.getGuest().getPhone() + "<br>\n" +
                "        Email: " + (reservation.getGuest().getEmail().isEmpty() ? "N/A" : reservation.getGuest().getEmail()) + "\n" +
                "      </div>\n" +
                "      <div class=\"guest-box\" style=\"text-align: right;\">\n" +
                "        <h3>Booking Status</h3>\n" +
                "        <span class=\"status-badge\">" + reservation.getStatus().getLabel().toUpperCase() + "</span><br>\n" +
                "        <small style=\"color: #64748b;\">" + payDetails + "</small>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <table class=\"table\">\n" +
                "      <thead>\n" +
                "        <tr>\n" +
                "          <th>Description</th>\n" +
                "          <th>Dates</th>\n" +
                "          <th>Rate</th>\n" +
                "          <th style=\"text-align: right;\">Amount</th>\n" +
                "        </tr>\n" +
                "      </thead>\n" +
                "      <tbody>\n" +
                "        <tr>\n" +
                "          <td><strong>Room " + reservation.getRoom().getRoomId() + "</strong> (" + reservation.getRoom().getType().getDisplayName() + ")</td>\n" +
                "          <td>" + reservation.getCheckInDate() + " to " + reservation.getCheckOutDate() + " (" + reservation.getNumberOfNights() + " nights)</td>\n" +
                "          <td>$" + String.format("%.2f", reservation.getRoom().getPricePerNight()) + " / night</td>\n" +
                "          <td style=\"text-align: right;\">$" + String.format("%.2f", subtotal) + "</td>\n" +
                "        </tr>\n" +
                "      </tbody>\n" +
                "    </table>\n" +
                "    <div class=\"total-box\">\n" +
                "      <div class=\"total-row\"><span>Room Subtotal:</span><span>$" + String.format("%.2f", subtotal) + "</span></div>\n" +
                "      <div class=\"total-row\"><span>Hospitality Tax (10%):</span><span>$" + String.format("%.2f", tax) + "</span></div>\n" +
                "      <div class=\"total-row grand\"><span>Total Due:</span><span>$" + String.format("%.2f", grandTotal) + "</span></div>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "      <p>Thank you for choosing Lumina Hotel. We look forward to hosting you!</p>\n" +
                "      <p style=\"font-size: 11px;\">Confirmation Barcode: ||| | | |||| || |||||| | ||| " + reservation.getReservationId() + "</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(html);
        } catch (Exception e) {
            System.err.println("Error saving invoice: " + e.getMessage());
        }

        return file;
    }

    public static void openInvoiceInBrowser(File file) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(file.toURI());
            }
        } catch (Exception e) {
            System.err.println("Could not open browser: " + e.getMessage());
        }
    }
}