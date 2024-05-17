package com.webapp.user;

import com.webapp.book.Book;
import com.webapp.book.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired private UserService userService;

    @Autowired private BookService bookService;


    @GetMapping("/index")
    public String goHome(){
        return "index";
    }

    @GetMapping("/users/new")
    public String goToRegisterPage(){
        return "user_form";
    }

    @GetMapping("/login")
    public String goToLogin(){
        return "login";
    }

    @GetMapping("/public/books")
    public String goToPublicBooks(){
        return "public_books";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("name") String name,
                               @RequestParam("surname") String surname,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("profilePicture") String profilePicture,
                               RedirectAttributes redirectAttributes) throws IOException {
        User user = new User(name, surname, email, password, profilePicture);


        userService.save(user);

        redirectAttributes.addFlashAttribute("message", "Successfully Registered!");
        return "redirect:/login";
    }

    @PostMapping("/Dashboard/Page")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            Model model, RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {
        User loggedInUser = userService.getLoginUser(email, password);
        if (loggedInUser != null) {
            User user = userService.getUserAttibutesAfterLogIn(email);

            // Start session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            model.addAttribute("user", user);
            return "dashboard";
        } else {
            redirectAttributes.addFlashAttribute("message", "Invalid username or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/login";
    }

    @GetMapping("/add/book")
    public String addBook(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();

        User user = (User)session.getAttribute("user");

        System.out.println(user.getId());

        session.setAttribute("user", user);

        model.addAttribute("user", user);

        return "add_book_form";
    }

    @PostMapping("/addbook")
    public String addBook(@RequestParam("name") String name,
                          @RequestParam("author") String author,
                          @RequestParam("availability") String availability,
                          @RequestParam("category") String category,
                          @RequestParam("pdfFile") MultipartFile pdfFile,
                          RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession();

        User user = (User)session.getAttribute("user");

        Book book = new Book(name, author, category, availability, user);

        if (!pdfFile.isEmpty()) {
            book.setPdfFile(pdfFile.getBytes());
        }

        bookService.save(book);

        session.setAttribute("user", user);

        model.addAttribute("user", user);

        redirectAttributes.addFlashAttribute("message", "Your book is saved successfully!");

        return "redirect:/dashboard";

    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // Redirect to login if the user is not logged in
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "dashboard";
    }

    @GetMapping("/view/books")
    public String goToViewBooks(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Retrieve books owned by the user
        List<Book> userBooks = bookService.findBooksByUser(user);

        session.setAttribute("user", user);

        model.addAttribute("user", user);

        model.addAttribute("userBooks", userBooks);
        return "choose_category_private_user";
    }


    @GetMapping("/book/{id}/download")
    public ResponseEntity<byte[]> downloadBook(@PathVariable Integer id) {
        // Retrieve the book from the database based on the ID
        Book book = bookService.findById(id);
        if (book == null) {
            // Return a 404 response if the book is not found
            return ResponseEntity.notFound().build();
        }

        // Set headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", book.getName() + ".pdf");
        headers.setContentLength(book.getPdfFile().length);

        // Return the PDF file as a byte array
        return ResponseEntity.ok()
                .headers(headers)
                .body(book.getPdfFile());
    }

    @GetMapping("/view/user/fiction")
    public String viewBooksFictionalBooks(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        List<Book> books = bookService.getBooksByCategoryAndUser("Fiction", user);

        session.setAttribute("user", user);

        model.addAttribute("user", user);
        model.addAttribute("books", books);

        return "fictional_books";
    }

    @GetMapping("/view/user/non/fiction")
    public String viewBooksNonFictionalBooks(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        List<Book> books = bookService.getBooksByCategoryAndUser("Non-Fiction", user);

        session.setAttribute("user", user);

        model.addAttribute("user", user);
        model.addAttribute("books", books);

        return "non_fictional_books";
    }

    @GetMapping("/view/user/children/books")
    public String viewBooksChildrenBooks(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        List<Book> books = bookService.getBooksByCategoryAndUser("Children's Books", user);

        session.setAttribute("user", user);

        model.addAttribute("user", user);
        model.addAttribute("books", books);

        return "children_books";
    }

    @GetMapping("/view/user/educational")
    public String viewBooksByEducational(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        List<Book> books = bookService.getBooksByCategoryAndUser("Educational", user);

        session.setAttribute("user", user);

        model.addAttribute("user", user);
        model.addAttribute("books", books);

        return "educational_books";
    }

    @GetMapping("/view/user/miscellaneous")
    public String viewBooksByMiscellaneous(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        List<Book> books = bookService.getBooksByCategoryAndUser("Miscellaneous", user);

        session.setAttribute("user", user);

        model.addAttribute("user", user);
        model.addAttribute("books", books);

        return "miscellaneous_books";
    }

    @GetMapping("/view/anonymous/fiction")
    public String viewBooksPublicFictionalBooks(Model model) {
        List<Book> books = bookService.findPublicFictionBooks("Fiction");
        model.addAttribute("books", books);
        return "public_fictional_books";
    }

    @GetMapping("/view/anonymous/non/fiction")
    public String viewBooksPublicNonFictionalBooks(Model model) {
        List<Book> books = bookService.findPublicFictionBooks("Non-Fiction");
        model.addAttribute("books", books);
        return "public_non_fictional_books";
    }

    @GetMapping("/view/anonymous/children/books")
    public String viewBooksPublicChildrenBooks(Model model) {
        List<Book> books = bookService.findPublicFictionBooks("Children's Books");
        model.addAttribute("books", books);
        return "public_children_books";
    }

    @GetMapping("/view/anonymous/educational")
    public String viewBooksPublicEducationalBooks(Model model) {
        List<Book> books = bookService.findPublicFictionBooks("Educational");
        model.addAttribute("books", books);
        return "public_educational_books";
    }

    @GetMapping("/view/anonymous/miscellaneous")
    public String viewBooksPublicMiscellaneousBooks(Model model) {
        List<Book> books = bookService.findPublicFictionBooks("Miscellaneous");
        model.addAttribute("books", books);
        return "public_miscellaneous_books";
    }

    @GetMapping("/delete/account")
    public String deleteAccount(HttpServletRequest request, RedirectAttributes redirectAttributes){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        bookService.removeBookByUser(user);
        userService.deleteAccount(user);
        redirectAttributes.addFlashAttribute("message", "Account Deleted!");
        return "redirect:/login";
    }


}
