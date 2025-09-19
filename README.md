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


  <img width="800" height="550" alt="Hình . Cấu trúc thư mục project multicast_chat_gui trên Visual Studio Code" src="https://github.com/user-attachments/assets/217caf09-8829-425b-b1db-02877a5dd43f" />
</p>
<p align="center"><b>Hình 1.</b> Giao diện phòng chat sử dụng UDP Multicast.</p>

<p align="center">
<p align="center">
  <img width="800" height="550" alt="Hình . Cấu trúc thư mục project multicast_chat_gui trên Visual Studio Code" src="https://github.com/user-attachments/assets/f4b42850-7144-4214-95f6-3aa8f0898b3b" />
</p>
<p align="center"><b>Hình 2.</b>Các user tham gia phòng chat sử dụng UDP Multicast.</p>
<p align="center">
<p align="center">
  <img width="800" height="550" alt="Hình . Cấu trúc thư mục project multicast_chat_gui trên Visual Studio Code" src="https://github.com/user-attachments/assets/0faad44b-6d14-491f-8b29-74975bdbd0b5" />
</p>
<p align="center"><b>Hình 3.</b> Các cửa sổ chat của nhiều client kết nối cùng một phòng chat sử dụng UDP Multicast.</p>
<p align="center">
  <img width="800" height="550" alt="Hình . Cấu trúc thư mục project multicast_chat_gui trên Visual Studio Code" src="https://github.com/user-attachments/assets/5dc017e8-12c0-4a4e-b368-f295075cb730" />
</p>
<p align="center"><b>Hình 4.</b> Các user rời khỏi phòng chat sử dụng UDP Multicast.</p>


## 4. Các bước cài đặt
### 🔧 Yêu cầu hệ thống

- **Java Development Kit (JDK)**: Phiên bản 8 trở lên
- **Hệ điều hành**: Windows, macOS, hoặc Linux
- **Môi trường phát triển**: IDE (IntelliJ IDEA, Eclipse, VS Code) hoặc terminal/command prompt
- **Bộ nhớ**: Tối thiểu 512MB RAM
- **Dung lượng**: Khoảng 10MB cho mã nguồn và file thực thi
### 📦 Cài đặt và triển khai

#### Bước 1: Chuẩn bị môi trường
1. **Kiểm tra Java**: Mở terminal/command prompt và chạy:
   ```bash
   java -version
   javac -version
   ```
   Đảm bảo cả hai lệnh đều hiển thị phiên bản Java 8 trở lên.
#### Bước 2: Biên dịch mã nguồn
1. **Mở terminal** và điều hướng đến thư mục chứa mã nguồn
2. **Biên dịch các file Java**:
   ```bash
   javac MulticastChatGUI.java
   ```
#### Bước 3: Chạy ứng dụng
**Khởi động chương trình**:
```bash
java MulticastChatGUI
```

- Một cửa sổ giao diện chat sẽ hiển thị.  
- Có thể mở **nhiều cửa sổ** trên cùng một máy, hoặc chạy trên **nhiều máy trong cùng mạng LAN**.  

---

### 🚀 Sử dụng ứng dụng
1. **Tham gia phòng chat**: Nhập tên người dùng khi giao diện yêu cầu.  
2. **Gửi tin nhắn**: Gõ tin nhắn vào ô nhập → nhấn Enter hoặc nút **Gửi**.  
3. **Nhận tin nhắn**: Tất cả các client trong cùng nhóm multicast sẽ nhận được.  
4. **Thoát**: Đóng cửa sổ để ngắt kết nối khỏi phòng chat.  

---
## Thông tin cá nhân
**Họ tên**: Bùi Anh Tuấn.  
**Lớp**: CNTT 16-03.  
**Email**: gaytongteo2004@gmail.com.

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

---
