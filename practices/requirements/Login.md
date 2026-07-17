# Module: Login

## Mục đích

Cho phép người dùng đăng nhập vào hệ thống.

---

## URL

Base URL

https://stg-crm.smarthiz.vn/

Path

/login

---

## Page

LoginPage

---

## Điều kiện

- User đã được tạo.
- Account đang Active.

---

## Business Rule

### Thành công

- Username hợp lệ
- Password hợp lệ
- Redirect Home

Tài khoản đăng nhập được lấy từ **testdata/user.json**

### Thất bại

Sai Username

Hiển thị

Invalid username

Sai Password

Hiển thị

Invalid password

Account Locked

Hiển thị

Account is locked

---

## Thành phần giao diện

Username textbox

Password textbox

Remember Me checkbox

Login button

Forgot Password link

Register link

---

## Điều hướng

Login

↓

Home

Forgot Password

↓

Forgot Password Page

Register

↓

Register Page