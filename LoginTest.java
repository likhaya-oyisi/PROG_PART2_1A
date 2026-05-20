package chatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @BeforeEach
    public void setUp() {
        // Register a valid user so loginUser() has stored credentials
        chatapp.Login login = new chatapp.Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        login.registerUser();
    }

    @Test
    public void testUsernameCorrectlyFormatted() {
        chatapp.Login login = new chatapp.Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        chatapp.Login login = new chatapp.Login("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertFalse(login.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexity() {
        chatapp.Login login = new chatapp.Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        chatapp.Login login = new chatapp.Login("kyl_1", "password", "+27838968976", "Kyle", "Smith");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        chatapp.Login login = new chatapp.Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        chatapp.Login login = new chatapp.Login("kyl_1", "Ch&&sec@ke99!", "08966553", "Kyle", "Smith");
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    public void testLoginSuccessful() {
        chatapp.Login login = new chatapp.Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.loginUser());
    }

    @Test
    public void testLoginFailed() {
        chatapp.Login login = new chatapp.Login("kyl_1", "wrongpassword", "+27838968976", "Kyle", "Smith");
        assertFalse(login.loginUser());
    }

    @Test
    public void testRegisterUserInvalidUsername() {
        chatapp.Login login = new chatapp.Login("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals(
                "Username is not correctly formatted; please ensure that your username " +
                        "contains an underscore and is no more than five characters in length.",
                login.registerUser()
        );
    }

    @Test
    public void testRegisterUserInvalidPassword() {
        chatapp.Login login = new chatapp.Login("kyl_1", "password", "+27838968976", "Kyle", "Smith");
        assertEquals(
                "Password is not correctly formatted; please ensure that the password " +
                        "contains at least eight characters, a capital letter, a number, and a special character.",
                login.registerUser()
        );
    }

    @Test
    public void testRegisterUserInvalidCellPhone() {
        chatapp.Login login = new chatapp.Login("kyl_1", "Ch&&sec@ke99!", "08966553", "Kyle", "Smith");
        assertEquals(
                "Cell number is incorrectly formatted or does not contain an international " +
                        "code; please correct the number and try again.",
                login.registerUser()
        );
    }

    @Test
    public void testReturnLoginStatusSuccess() {
        chatapp.Login login = new chatapp.Login("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals("Welcome Kyle Smith it is great to see you again.", login.returnLoginStatus());
    }

    @Test
    public void testReturnLoginStatusFailed() {
        chatapp.Login login = new chatapp.Login("kyl_1", "wrongPass1!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus());
    }
}