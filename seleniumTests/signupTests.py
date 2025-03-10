import time
import sys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait, Select
from selenium.webdriver.support import expected_conditions as EC

def go_to_signup(browser):
    """
    Navigates to the signup page by clicking the "Get Started" button.
    """
    # Wait for and click the "Get Started" button (assumed to be on the landing page)
    signUpBtn = WebDriverWait(browser, 10).until(
        EC.element_to_be_clickable((By.CLASS_NAME, "get-started-btn"))
    )
    signUpBtn.click()
    # Wait until the URL contains '/signup'
    WebDriverWait(browser, 10).until(lambda b: "/signup" in b.current_url)
    time.sleep(1)  # extra wait for animations if necessary

def fill_signup_form(browser, firstname, lastname, personNumber, username, email, password):
    """
    Fills in the signup form fields.
    """
    # Wait for fields to be present
    firstNameField = WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((By.NAME, "firstname"))
    )
    lastNameField = browser.find_element(By.NAME, "lastname")
    personNumberField = browser.find_element(By.NAME, "personNumber")
    usernameField = browser.find_element(By.NAME, "username")
    emailField = browser.find_element(By.NAME, "email")
    passwordField = browser.find_element(By.NAME, "password")
    
    # Clear any existing text (if needed)
    firstNameField.clear()
    lastNameField.clear()
    personNumberField.clear()
    usernameField.clear()
    emailField.clear()
    passwordField.clear()
    
    firstNameField.send_keys(firstname)
    lastNameField.send_keys(lastname)
    personNumberField.send_keys(personNumber)
    usernameField.send_keys(username)
    emailField.send_keys(email)
    passwordField.send_keys(password)

def click_register(browser):
    """
    Clicks the register button.
    """
    registerBtn = browser.find_element(By.CSS_SELECTOR, "button[type='submit']")
    registerBtn.click()
    time.sleep(1)  # Allow time for submission

def check_error_in_page(browser, errorText):
    """
    Checks if the expected error text is present in the page source.
    """
    page_source = browser.page_source
    return errorText in page_source

def testSignupScenarios(browser):
    """
    Runs several signup scenarios:
    1. Submitting an empty form.
    2. Submitting a form with invalid personNumber and email.
    3. Successful registration with valid data.
    4. Attempting duplicate registration (which should fail).
    """
    # ----- Scenario 1: Empty Form Submission -----
    go_to_signup(browser)
    # Submit without filling any fields:
    click_register(browser)
    time.sleep(1)
    
    # Check for required field errors (adjust text as per your UI)
    requiredErrors = ["First name is required.", "Last name is required.", 
                      "Personnumber is required.", "Username is required.", 
                      "Email is required.", "Password is required."]
    for err in requiredErrors:
        if not check_error_in_page(browser, err):
            print(f"Expected error '{err}' not found for empty form submission", file=sys.stderr, flush=True)
            sys.exit(1)
    print("Scenario 1 passed: Empty form errors displayed.")
    
    # ----- Scenario 2: Invalid Data -----
    # Refresh or navigate again to the signup page.
    browser.get(browser.current_url)  # reload the signup page
    time.sleep(1)
    
    # Fill form with invalid personNumber and email formats.
    fill_signup_form(browser, "John", "Doe", "invalidPN", "johndoe", "invalidemail", "password123")
    click_register(browser)
    time.sleep(1)
    if not check_error_in_page(browser, "Invalid format (expected YYYYMMDD-XXXX)"):
        print("Expected error for invalid personNumber not found", file=sys.stderr, flush=True)
        sys.exit(1)
    if not check_error_in_page(browser, "Invalid email format"):
        print("Expected error for invalid email not found", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 2 passed: Invalid data errors displayed.")
    
    # ----- Scenario 3: Successful Registration -----
    # Navigate again to signup page (or refresh)
    browser.get(browser.current_url)
    time.sleep(1)
    validPersonNumber = "19900101-1234"  # valid format: YYYYMMDD-XXXX
    validEmail = "john.doe@example.com"
    fill_signup_form(browser, "John", "Doe", validPersonNumber, "johndoe", validEmail, "password123")
    click_register(browser)
    # Wait until redirected to signin page.
    WebDriverWait(browser, 10).until(lambda b: "/signin" in b.current_url)
    if "/signin" not in browser.current_url:
        print("Successful registration did not redirect to signin", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 3 passed: Successful registration redirected to signin.")
    
    # ----- Scenario 4: Duplicate Registration -----
    # Navigate to signup page again
    go_to_signup(browser)
    fill_signup_form(browser, "John", "Doe", validPersonNumber, "johndoe", validEmail, "password123")
    click_register(browser)
    time.sleep(1)
    # Expecting duplicate registration failure message.
    if not check_error_in_page(browser, "Registration failed"):
        print("Duplicate registration did not fail as expected", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 4 passed: Duplicate registration properly failed.")

if __name__ == "__main__":
    # The main test runner; assumes two arguments: browser and URL.
    import selenium.webdriver as webDriver
    from selenium.webdriver.chrome.options import Options as chromeOptions
    from selenium.webdriver.firefox.options import Options as firefoxOptions
    from selenium.webdriver.edge.options import Options as edgeOptions
    from selenium.webdriver.safari.options import Options as safariOptions

    if len(sys.argv) != 3:
        print("Usage: python3 signupTests.py <browser> <url>", file=sys.stderr, flush=True)
        sys.exit(1)
    
    browser_type = sys.argv[1]
    if browser_type == "chrome":
        opts = chromeOptions()
        browser = webDriver.Chrome(options=opts)
    elif browser_type == "firefox":
        opts = firefoxOptions()
        browser = webDriver.Firefox(options=opts)
    elif browser_type == "edge":
        opts = edgeOptions()
        browser = webDriver.Edge(options=opts)
    elif browser_type == "safari":
        opts = safariOptions()
        browser = webDriver.Safari(options=opts)
    else:
        print("Invalid browser", file=sys.stderr, flush=True)
        sys.exit(1)
    
    url = sys.argv[2]
    browser.get(url)
    
    try:
        testSignupScenarios(browser)
        print("ALL SIGNUP TEST SCENARIOS PASSED", flush=True)
    except Exception as e:
        print("TEST FAILURE:", e, file=sys.stderr, flush=True)
        sys.exit(1)
    finally:
        time.sleep(2)
        browser.quit()
