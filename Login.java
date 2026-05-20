package chatapp;

public class Login {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Stored credentials set after successful registration
    private static String registeredUsername;
    private static String registeredPassword;
    private static String registeredFirstName;
    private static String registeredLastName;

    public Login(String username, String password, String cellPhoneNumber,
                 String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Checks that the username contains an underscore and is no more than five characters long.
    public boolean checkUserName() {
        if (username == null) return false;
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks password complexity:
     * at least 8 chars, one capital, one number, one special character.
     */
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) return false;

        boolean hasCapital = false, hasNumber = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))       hasCapital = true;
            else if (Character.isDigit(c))      hasNumber  = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Checks cell phone number format using a regular expression.
     * Must start with '+' followed by up to 10 digits.
     */
    public boolean checkCellPhoneNumber() {
        if (cellPhoneNumber == null) return false;
        return cellPhoneNumber.matches("^\\+\\d{1,10}$");
    }

    /**
     * Registers the user after validating all fields.
     */
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username " +
                    "contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password " +
                    "contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell number is incorrectly formatted or does not contain an international " +
                    "code; please correct the number and try again.";
        }

        registeredUsername  = this.username;
        registeredPassword  = this.password;
        registeredFirstName = this.firstName;
        registeredLastName  = this.lastName;

        return "Username successfully captured.\n" +
                "Password successfully captured.\n" +
                "Cell number successfully captured.\n" +
                "Registration successful!";
    }

    /**
     * Returns true if the entered credentials match the registered credentials.
     */
    public boolean loginUser() {
        if (registeredUsername == null || registeredPassword == null) return false;
        return this.username.equals(registeredUsername) &&
                this.password.equals(registeredPassword);
    }

    /**
     * Returns the login status message.
     */
    public String returnLoginStatus() {
        if (loginUser()) {
            return "Welcome " + registeredFirstName + " " + registeredLastName +
                    " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    // Getters
    public String getUsername()        { return username; }
    public String getPassword()        { return password; }
    public String getCellPhoneNumber() { return cellPhoneNumber; }
    public String getFirstName()       { return firstName; }
    public String getLastName()        { return lastName; }
}