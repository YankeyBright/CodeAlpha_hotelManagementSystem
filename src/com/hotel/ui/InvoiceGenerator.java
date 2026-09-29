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
 * Generates an ultra-premium, luxury printable HTML invoice with Ghana Cedis (GH₵) formatting.
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

        String payDetails = "Payment Due on Arrival";
        if (payments != null && !payments.isEmpty()) {
            Payment p = payments.get(payments.size() - 1);
            payDetails = "PAID via " + p.getMethod().getLabel() + " (Ref: " + p.getTransactionRef() + ")";
        }

        double subtotal = reservation.getTotalPrice();
        double vat = subtotal * 0.15; // 15% VAT / NHIL / GETFund
        double grandTotal = subtotal + vat;

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>Official Invoice - " + reservation.getReservationId() + "</title>\n" +
                "  <style>\n" +
                "    body { font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, Roboto, sans-serif; margin: 0; padding: 40px; background: #f8fafc; color: #0f172a; }\n" +
                "    .invoice-card { max-width: 720px; margin: auto; background: #ffffff; border: 1px solid #e2e8f0; border-radius: 8px; padding: 40px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }\n" +
                "    .header { display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 2px solid #0f172a; padding-bottom: 24px; }\n" +
                "    .brand h1 { margin: 0; font-size: 24px; letter-spacing: 0.5px; color: #0f172a; }\n" +
                "    .brand p { margin: 6px 0 0; font-size: 13px; color: #64748b; line-height: 1.5; }\n" +
                "    .inv-meta { text-align: right; }\n" +
                "    .inv-meta h2 { margin: 0; font-size: 20px; color: #c59b27; letter-spacing: 0.5px; }\n" +
                "    .inv-meta p { margin: 6px 0 0; font-size: 13px; color: #64748b; }\n" +
                "    .guest-grid { display: flex; justify-content: space-between; margin: 28px 0; font-size: 13px; }\n" +
                "    .guest-box h3 { margin: 0 0 6px; font-size: 11px; text-transform: uppercase; letter-spacing: 0.6px; color: #64748b; }\n" +
                "    .table { width: 100%; border-collapse: collapse; margin: 24px 0; font-size: 13px; }\n" +
                "    .table th { text-align: left; padding: 12px; background: #f8fafc; border-bottom: 1px solid #e2e8f0; color: #475569; font-weight: 600; text-transform: uppercase; font-size: 11px; letter-spacing: 0.5px; }\n" +
                "    .table td { padding: 14px 12px; border-bottom: 1px solid #f1f5f9; }\n" +
                "    .total-box { margin-left: auto; width: 300px; font-size: 13px; margin-top: 20px; }\n" +
                "    .total-row { display: flex; justify-content: space-between; padding: 6px 0; }\n" +
                "    .total-row.grand { border-top: 2px solid #0f172a; padding-top: 12px; font-size: 17px; font-weight: bold; color: #0f172a; }\n" +
                "    .status-badge { display: inline-block; padding: 4px 12px; border-radius: 4px; font-size: 12px; font-weight: 600; background: #ecfdf5; color: #059669; }\n" +
                "    .footer { margin-top: 40px; padding-top: 24px; border-top: 1px solid #e2e8f0; text-align: center; font-size: 12px; color: #94a3b8; }\n" +
                "    .print-btn { display: inline-block; background: #0f172a; color: white; border: none; padding: 10px 22px; border-radius: 6px; font-weight: 600; cursor: pointer; text-decoration: none; margin-bottom: 20px; }\n" +
                "    @media print { .print-btn { display: none; } body { padding: 0; background: white; } .invoice-card { box-shadow: none; border: none; padding: 0; } }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div style=\"max-width: 720px; margin: auto; text-align: right;\">\n" +
                "    <button class=\"print-btn\" onclick=\"window.print()\">Print / Save as PDF</button>\n" +
                "  </div>\n" +
                "  <div class=\"invoice-card\">\n" +
                "    <div class=\"header\">\n" +
                "      <div class=\"brand\">\n" +
                "        <h1>LUMINA HOTEL & RESIDENCES</h1>\n" +
                "        <p>12 Senchi Street, Airport Residential Area, Accra, Ghana<br>concierge@luminahotel.com.gh • +233 (0) 30 277 8899<br>GRA TIN: C0029841284</p>\n" +
                "      </div>\n" +
                "      <div class=\"inv-meta\">\n" +
                "        <h2>OFFICIAL INVOICE</h2>\n" +
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
                "        <h3>Reservation Status</h3>\n" +
                "        <span class=\"status-badge\">" + reservation.getStatus().getLabel().toUpperCase() + "</span><br>\n" +
                "        <small style=\"color: #64748b; margin-top: 4px; display: inline-block;\">" + payDetails + "</small>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <table class=\"table\">\n" +
                "      <thead>\n" +
                "        <tr>\n" +
                "          <th>Accommodations</th>\n" +
                "          <th>Dates</th>\n" +
                "          <th>Nightly Rate</th>\n" +
                "          <th style=\"text-align: right;\">Amount</th>\n" +
                "        </tr>\n" +
                "      </thead>\n" +
                "      <tbody>\n" +
                "        <tr>\n" +
                "          <td><strong>Room " + reservation.getRoom().getRoomId() + "</strong> (" + reservation.getRoom().getType().getDisplayName() + ")</td>\n" +
                "          <td>" + reservation.getCheckInDate() + " to " + reservation.getCheckOutDate() + " (" + reservation.getNumberOfNights() + " nights)</td>\n" +
                "          <td>GH₵ " + String.format("%,.2f", reservation.getRoom().getPricePerNight()) + "</td>\n" +
                "          <td style=\"text-align: right;\">GH₵ " + String.format("%,.2f", subtotal) + "</td>\n" +
                "        </tr>\n" +
                "      </tbody>\n" +
                "    </table>\n" +
                "    <div class=\"total-box\">\n" +
                "      <div class=\"total-row\"><span>Room Subtotal:</span><span>GH₵ " + String.format("%,.2f", subtotal) + "</span></div>\n" +
                "      <div class=\"total-row\"><span>Hospitality Levy & VAT (15%):</span><span>GH₵ " + String.format("%,.2f", vat) + "</span></div>\n" +
                "      <div class=\"total-row grand\"><span>Total Due:</span><span>GH₵ " + String.format("%,.2f", grandTotal) + "</span></div>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "      <p>Thank you for choosing Lumina Hotel & Residences. We look forward to hosting your stay!</p>\n" +
                "      <p style=\"font-size: 11px;\">Digital Verification Code: LUM-" + reservation.getReservationId() + "-ACCRA</p>\n" +
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