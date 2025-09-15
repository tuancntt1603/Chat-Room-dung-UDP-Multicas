<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   CHAT ROOM DÙNG UDP MULTICAST
</h2>
<div align="center">
    <p align="center">
        <img alt="AIoTLab Logo" width="170" src="https://github.com/user-attachments/assets/711a2cd8-7eb4-4dae-9d90-12c0a0a208a2" />
        <img alt="AIoTLab Logo" width="180" src="https://github.com/user-attachments/assets/dc2ef2b8-9a70-4cfa-9b4b-f6c2f25f1660" />
        <img alt="DaiNam University Logo" width="200" src="https://github.com/user-attachments/assets/77fe0fd1-2e55-4032-be3c-b1a705a1b574" />
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

---
## 📖 1. Giới thiệu
Học phần trang bị cho người học những kiến thức nền tảng của lập trình mạng và các kỹ năng cần thiết để thiết kế và cài đặt các ứng dụng mạng và các chuẩn ở mức ứng dụng dựa trên mô hình Client/Server, có sử dụng các giao tiếp chương trình dựa trên Sockets. Kết thúc học phần, sinh viên có thể viết các chương trình ứng dụng mạng với giao thức tầng ứng dụng tự thiết kế.
## 🔧 2. Công nghệ và ngôn ngữ lập trình sử dụng
🔹 Ngôn ngữ lập trình sử dụng

Java

Dùng Java SE (Standard Edition), phiên bản phổ biến (Java 8 trở lên).

Thư viện sử dụng:

java.net.* (UDP, DatagramSocket, MulticastSocket, InetAddress).

javax.swing.* (tạo giao diện đồ họa).

java.awt.* (hỗ trợ bố cục giao diện).

🔹 Môi trường lập trình

IDE: Eclipse IDE (Eclipse IDE for Java Developers).

Hệ điều hành: Windows.

JDK: JDK 17

Project Type: Java Project (trong Eclipse).
## 🚀 3. Hình ảnh các chức năng
<p align="center">
  <img width="679" height="461" alt="Hình 1. Giao diện ứng dụng Chat Room sử dụng UDP Multicast" src="https://github.com/user-attachments/assets/6819595d-e265-426b-bbc9-4ae5e4c87b93" />
</p>
<p align="center"><b>Hình 1.</b> Giao diện ứng dụng Chat Room sử dụng UDP Multicast</p>

<p align="center">
  <img width="800" height="550" alt="Hình . Cấu trúc thư mục project multicast_chat_gui trên Visual Studio Code" src="https://github.com/user-attachments/assets/99f9e2f0-e367-4436-9f13-703ec124faa3" />
</p>
<p align="center"><b>Hình 2.</b>  Các cửa sổ chat của nhiều client kết nối cùng một phòng chat sử dụng UDP Multicast.</p>


## 🚀 4. Các project đã thực hiện
```
multicast_chat_gui/
├── src/
│   └── chat/
│       ├── MulticastChatGUI.java   // Main + GUI
│       ├── Sender.java             // Xử lý gửi tin nhắn
│       └── Receiver.java           // Xử lý nhận tin nhắn
```
## Cách chạy
 -Biên dịch code:
   ```bash
   javac -d bin src/chat/*.java
   ```
-Chạy chương trình:
   ```bash
   java -cp bin chat.MulticastChatGUI
   ```
-Mở nhiều cửa sổ  để tham gia chat room
## Thông tin cá nhân
**Họ tên**: Bùi Anh Tuấn.  
**Lớp**: CNTT 16-03.  
**Email**: gaytongteo2004@gmail.com.

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

---
