import com.github.bluesgao.jsm.example.UserService;

public class App {
    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.getUserName("1111");
        userService.setUserName("aaaa");
        userService.getUserName2("2222");
        userService.setUserName2("bbbb");
    }
}
