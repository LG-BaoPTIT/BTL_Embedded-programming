package com.example.spring.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    public void sendFireAlertEmail(String to) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("CẢNH BÁO CHÁY!");

            String htmlContent ="<!DOCTYPE html>\n" +
                    "<html lang=\"vi\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Cảnh Báo Cháy</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: 'Arial', sans-serif;\n" +
                    "            background-color: #f7f7f7;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "            display: flex;\n" +
                    "            align-items: center;\n" +
                    "            justify-content: center;\n" +
                    "            height: 100vh;\n" +
                    "        }\n" +
                    "\n" +
                    "        .alert-container {\n" +
                    "            max-width: 400px;\n" +
                    "            background-color: #fff;\n" +
                    "            border-radius: 10px;\n" +
                    "            overflow: hidden;\n" +
                    "            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);\n" +
                    "        }\n" +
                    "\n" +
                    "        .alert-header {\n" +
                    "            background-color: #d9534f;\n" +
                    "            color: #fff;\n" +
                    "            padding: 20px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "\n" +
                    "        .alert-header h1 {\n" +
                    "            margin: 0;\n" +
                    "            font-size: 24px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .alert-content {\n" +
                    "            padding: 20px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "\n" +
                    "        .alert-message {\n" +
                    "            font-size: 16px;\n" +
                    "            color: #333;\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .instructions {\n" +
                    "            font-size: 14px;\n" +
                    "            color: #555;\n" +
                    "            text-align: left;\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .alert-button {\n" +
                    "            background-color: #d9534f;\n" +
                    "            color: #fff;\n" +
                    "            padding: 15px 40px;\n" +
                    "            text-decoration: none;\n" +
                    "            display: inline-block;\n" +
                    "            border-radius: 5px;\n" +
                    "            font-size: 18px;\n" +
                    "            font-weight: bold;\n" +
                    "            cursor: pointer;\n" +
                    "            display: block;\n" +
                    "            margin: 0 auto; /* Đặt nút ở giữa */\n" +
                    "        }\n" +
                    "\n" +
                    "        .alert-button:hover {\n" +
                    "            background-color: #c9302c;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                    "<div class=\"alert-container\">\n" +
                    "    <div class=\"alert-header\">\n" +
                    "        <h1>Cảnh Báo Cháy!</h1>\n" +
                    "    </div>\n" +
                    "    <div class=\"alert-content\">\n" +
                    "        <div class=\"alert-message\">Phát hiện rò rỉ khí gas. Vui lòng thực hiện các bước khẩn cấp sau:</div>\n" +
                    "        <div class=\"instructions\">\n" +
                    "            <p>- Khẩn trương mở cửa sổ và cửa ra vào để tăng cường thông gió.</p>\n" +
                    "            <p>- Đeo khẩu trang và mũ bảo hộ nếu có.</p>\n" +
                    "            <p>- Không sử dụng các thiết bị điện và không thực hiện bất kỳ bước hành động nào tạo ra lửa hoặc tia lửa.</p>\n" +
                    "            <p>- Nếu bạn biết cách sử dụng, tắt nguồn khí gas từ bảng điều khiển hoặc van khẩn cấp.</p>\n" +
                    "            <p>- Gọi ngay cho dịch vụ cứu thương hoặc cảnh sát bằng cách nhấn vào nút bên dưới.</p>\n" +
                    "        </div>\n" +
                    "        <a href=\"tel:114\" class=\"alert-button\">Gọi 114 Ngay</a>\n" +
                    "    </div>\n" +
                    "</div>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n";
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
