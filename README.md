# ReadWell - Your Personal Library

ReadWell is a web application that allows users to create their own personal library where they can upload and manage their books privately, or share them with the public.

## Features

- User Registration: Users can create an account to access the platform.
- Password Reset: Users can reset their password if they forget it.
- Book Management: Users can upload, edit, and delete books from their personal library.
- Profile Customization: Users can customize their profile with a profile picture.
- Secure Authentication: User passwords are encrypted using BCryptPasswordEncoder.
- Email Notifications: Users receive email notifications for account registration and password reset.
- Responsive Design: The application is designed to be responsive and compatible with various devices.

## Technologies Used

- Spring Boot: Java-based framework for building web applications.
- Spring Security: Provides authentication and access control features.
- Thymeleaf: Template engine for server-side rendering.
- SendGrid: Email delivery platform for sending transactional and marketing emails.

## Getting Started

To run this project locally, follow these steps:

1. Clone this repository: `git clone https://github.com/yourusername/readwell.git`
2. Navigate to the project directory: `cd readwell`
3. Configure your SendGrid API key in `application.properties`.
4. Build and run the project using Maven: `mvn spring-boot:run`
5. Access the application in your web browser at `http://localhost:8080`
