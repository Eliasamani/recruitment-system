import sys
import time
import getpass  # To securely input correct password manually
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium import webdriver

def go_to_signin(browser, base_url):
    """Navigates to the sign-in page."""
    browser.get(base_url + "/signin")
    WebDriverWait(browser, 10).until(lambda b: "/signin" in b.current_url)
    time.sleep(1)

def attempt_invalid_signin(browser, username, password):
    """Attempts to log in with invalid credentials."""
    browser.find_element(By.NAME, "username").send_keys(username)
    browser.find_element(By.NAME, "password").send_keys(password)
    browser.find_element(By.CSS_SELECTOR, "button[type='submit']").click()
    time.sleep(2)
    if "Login failed" not in browser.page_source:
        print("Scenario 1 failed: No error on invalid login", file=sys.stderr)
        sys.exit(1)
    print("Scenario 1 passed: Login failed as expected.")

def go_to_forgot_password(browser):
    """Clicks 'Forgot Password?' link and verifies redirection."""
    browser.find_element(By.LINK_TEXT, "Forgot password?").click()
    WebDriverWait(browser, 10).until(lambda b: "/forgot-password" in b.current_url)
    if "/forgot-password" not in browser.current_url:
        print("Scenario 2 failed: Not redirected to forgot-password", file=sys.stderr)
        sys.exit(1)
    print("Scenario 2 passed: Redirected to Forgot Password page.")

def request_reset_code(browser, email):
    """Requests a reset code using the provided email."""
    email_field = browser.find_element(By.XPATH,"//input[@type='email']")
    email_field.clear()
    email_field.send_keys(email)
    browser.find_element(By.CSS_SELECTOR, "button[type='submit']").click()
    time.sleep(2)

def verify_error_message(browser, expected_message):
    """Checks if an expected error message is displayed on the page."""
    if expected_message.lower() not in browser.page_source.lower():
        print(f"Scenario failed: Expected error '{expected_message}' not found", file=sys.stderr)
        sys.exit(1)
    print(f"Scenario passed: '{expected_message}' detected.")

def reset_password(browser, email, username, new_password, reset_code):
    """Completes the reset password process."""
    section = browser.find_element(By.XPATH, "//h2[text()='Reset Credentials']/ancestor::section")
    email_field = section.find_element(By.XPATH, ".//input[@type='email']")
    email_field.clear()
    email_field.send_keys(email)
    username_field = section.find_element(By.XPATH, ".//input[@type='text' and @placeholder='Enter new username (optional)']")
    username_field.clear()
    username_field.send_keys(username)
    password_field = section.find_element(By.XPATH, ".//input[@type='password']")
    password_field.clear()
    password_field.send_keys(new_password)
    code_field = section.find_element(By.XPATH, ".//input[@type='text' and @placeholder='Enter the reset code']")
    code_field.clear()
    code_field.send_keys(reset_code)
    section.find_element(By.TAG_NAME, 'button').click()


def verify_reset_success(browser):
    """Checks if the reset was successful."""
    if "Reset successful" not in browser.page_source:
        print("Scenario failed: Password reset was not successful", file=sys.stderr)
        sys.exit(1)
    print("Scenario passed: Password reset successful.")

def verify_login_with_new_password(browser, username, password,base_url):
    """Attempts login with new credentials."""
    go_to_signin(browser, base_url)
    time.sleep(2)
    browser.find_element(By.NAME, "username").send_keys(username)
    browser.find_element(By.NAME, "password").send_keys(password)
    browser.find_element(By.CSS_SELECTOR, "button[type='submit']").click()
    WebDriverWait(browser, 10).until(lambda b: "/dashboard" in b.current_url)
    if "/dashboard" not in browser.current_url:
        print("Scenario failed: Unable to log in with new credentials", file=sys.stderr)
        sys.exit(1)
    print("Scenario passed: Successfully logged in with new credentials.")
def forgot_password_tests(browser,base_url):
        # Scenario 1: Attempt invalid sign-in
    go_to_signin(browser, base_url)
    attempt_invalid_signin(browser, "wrongUser", "wrongPass")

    # Scenario 2: Click Forgot Password
    go_to_forgot_password(browser)


    # Scenario 4: Request reset code with an unregistered email
    request_reset_code(browser, "unknown@example.com")
    verify_error_message(browser, "If this email is registered")

    # Scenario 5: Request reset code with a valid email
    request_reset_code(browser, "appltest@example.com")
    verify_error_message(browser, "A reset code has been sent.")


    # Scenario 6: Reset password with wrong code
    reset_password(browser, "appltest@example.com", "TestUser", "1234567", "999999")
    time.sleep(10)
    verify_error_message(browser, "Reset failed.")
    

    # Scenario 7: Reset password with correct code
    correct_reset_code = int(input("Enter the correct reset code: "))
    reset_password(browser, "appltest@example.com", "TestUser", "correctPassword", correct_reset_code)
    time.sleep(10)
    verify_reset_success(browser)

    # Scenario 8: Verify login with new credentials
    
    verify_login_with_new_password(browser, "TestUser", "correctPassword",base_url)

    print("ALL FORGOT PASSWORD TEST SCENARIOS PASSED.")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python3 forgotPasswordTests.py <browser> <base_url>", file=sys.stderr)
        sys.exit(1)

    browser_type = sys.argv[1]
    base_url = sys.argv[2]

    if browser_type == "chrome":
        options = webdriver.ChromeOptions()
        browser = webdriver.Chrome(options=options)
    elif browser_type == "firefox":
        options = webdriver.FirefoxOptions()
        browser = webdriver.Firefox(options=options)
    else:
        print("Invalid browser specified", file=sys.stderr)
        sys.exit(1)

    try:
        forgot_password_tests(browser,base_url)
    except Exception as e:
        print("TEST FAILURE:", e, file=sys.stderr)
        sys.exit(1)
    finally:
        time.sleep(2)
        browser.quit()
