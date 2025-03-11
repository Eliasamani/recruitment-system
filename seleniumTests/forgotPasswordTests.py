import time
import sys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def go_to_forgot_password(browser, base_url):
    """
    Navigates to the forgot-password page.
    Adjust if your route is different (e.g. '/forgot-password').
    """
    browser.get(base_url + "/forgot-password")
    WebDriverWait(browser, 10).until(lambda b: "/forgot-password" in b.current_url)
    time.sleep(1)  # Allow time for animations if necessary

def fill_request_code_form(browser, email):
    """
    Fills and submits the "Request Reset Code" form.
    """
    email_field = WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((By.CSS_SELECTOR, "section.form-section input[type='email']"))
    )
    email_field.clear()
    email_field.send_keys(email)

    send_code_btn = browser.find_element(By.CSS_SELECTOR, "section.form-section button.submit-button")
    send_code_btn.click()
    time.sleep(1)

def fill_reset_form(browser, reset_email, username, new_password, reset_code):
    """
    Fills and submits the "Reset Credentials" form.
    Only available after codeSent is true (i.e., after a successful code request).
    """
    # Wait for the second form to appear
    WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((By.CSS_SELECTOR, "section.form-section:nth-of-type(2)"))
    )

    reset_email_field = browser.find_element(By.NAME, "resetEmail")  # matches the second form's "Email:" input
    username_field = browser.find_element(By.NAME, "username")
    password_field = browser.find_element(By.NAME, "newPassword")
    code_field = browser.find_element(By.NAME, "resetCode")

    reset_email_field.clear()
    reset_email_field.send_keys(reset_email)
    username_field.clear()
    username_field.send_keys(username)
    password_field.clear()
    password_field.send_keys(new_password)
    code_field.clear()
    code_field.send_keys(reset_code)

    reset_btn = browser.find_element(By.CSS_SELECTOR, "section.form-section:nth-of-type(2) button.submit-button")
    reset_btn.click()
    time.sleep(1)

def check_in_page_source(browser, text):
    """
    Returns True if the provided text is found in the page source (case-insensitive).
    """
    return text.lower() in browser.page_source.lower()

def test_forgot_password_scenarios(browser, base_url):
    """
    Comprehensive scenarios for the Forgot Password flow:

    1. Request reset code with empty email → check for 'Email is required.' error.
    2. Request reset code with invalid email format → check for 'Invalid email format' error.
    3. Request reset code with presumably unregistered email → expects either an error or success message.
    4. Request reset code with a valid/registered email → expects success message + codeSent => second form appears.
    5. Attempt to reset with empty fields => check for required field error messages.
    6. Attempt to reset with invalid email format => check for 'Invalid email format'.
    7. Attempt to reset with wrong code => check for 'Reset failed.' or similar error from the server.
    8. Finally, reset with correct data => expect 'Reset successful.'
    """

    # --- Scenario 1: Empty Email ---
    go_to_forgot_password(browser, base_url)
    fill_request_code_form(browser, "")
    # Check for "Email is required." in the page (your app might differ)
    if not check_in_page_source(browser, "Email is required"):
        print("Scenario 1 failed: Did not find 'Email is required.' for empty email", file=sys.stderr)
        sys.exit(1)
    print("Scenario 1 passed: Empty email triggered 'Email is required.' error.")

    # --- Scenario 2: Invalid Email Format ---
    go_to_forgot_password(browser, base_url)
    fill_request_code_form(browser, "invalidEmail")
    if not check_in_page_source(browser, "Invalid email format"):
        print("Scenario 2 failed: Did not find 'Invalid email format' error", file=sys.stderr)
        sys.exit(1)
    print("Scenario 2 passed: Invalid email format triggered an error.")

    # --- Scenario 3: Unregistered Email ---
    # Adjust to your app's behavior: some apps display a success message even for unregistered emails,
    # while others display "Failed to send reset code."
    go_to_forgot_password(browser, base_url)
    fill_request_code_form(browser, "doesnotexist@example.com")
    if check_in_page_source(browser, "Failed to send reset code"):
        print("Scenario 3 partial pass: Unregistered email => 'Failed to send reset code' message displayed.")
    else:
        # Or your app might show "If this email is registered, a reset code has been sent."
        if check_in_page_source(browser, "If this email is registered, a reset code has been sent"):
            print("Scenario 3 partial pass: Unregistered email => 'If this email is registered...' shown.")
        else:
            print("Scenario 3 failed: No expected error or success message found for unregistered email", file=sys.stderr)
            sys.exit(1)

    # --- Scenario 4: Valid/Registered Email => codeSent => second form displayed ---
    # We'll assume "testforgot@example.com" is a valid/registered email for this scenario
    go_to_forgot_password(browser, base_url)
    fill_request_code_form(browser, "testforgot@example.com")
    # Expect success message or no error
    if check_in_page_source(browser, "Failed to send reset code"):
        print("Scenario 4 failed: 'Failed to send reset code' for a presumably registered email", file=sys.stderr)
        sys.exit(1)
    if not check_in_page_source(browser, "reset code has been sent") and not check_in_page_source(browser, "success"):
        print("Scenario 4 warning: Did not find a success message, but continuing if second form is displayed.")
    # Now the second form is displayed because the presenter's state codeSent = true
    if not check_in_page_source(browser, "Reset Credentials"):
        print("Scenario 4 failed: The second form for 'Reset Credentials' did not appear", file=sys.stderr)
        sys.exit(1)
    print("Scenario 4 passed: Valid email => second form appears (codeSent).")

    # --- Scenario 5: Reset form with empty required fields (email, reset code) ---
    fill_reset_form(browser, "", "", "", "")  # All empty
    time.sleep(1)
    # We expect "Email is required." or "Reset code is required."
    # The view / model specifically checks email and resetCode are required
    if not (check_in_page_source(browser, "Email is required") or check_in_page_source(browser, "Reset code is required")):
        print("Scenario 5 failed: Did not see required field errors for empty reset form", file=sys.stderr)
        sys.exit(1)
    print("Scenario 5 passed: Empty reset form triggered required field errors.")

    # --- Scenario 6: Reset form with invalid email format ---
    fill_reset_form(browser, "invalidemail", "someUser", "NewPass123", "123456")
    time.sleep(1)
    if not check_in_page_source(browser, "Invalid email format"):
        print("Scenario 6 failed: Did not find 'Invalid email format' for invalid reset email", file=sys.stderr)
        sys.exit(1)
    print("Scenario 6 passed: Invalid reset email triggered 'Invalid email format' error.")

    # --- Scenario 7: Reset form with wrong code ---
    # This depends on how your back-end responds if you provide an incorrect code. 
    # Typically, you'd get a 400 or 404 => "Reset failed."
    fill_reset_form(browser, "testforgot@example.com", "someUser", "NewPass123", "WRONGCODE")
    time.sleep(1)
    if not check_in_page_source(browser, "Reset failed"):
        print("Scenario 7 failed: Did not find 'Reset failed' when using incorrect code", file=sys.stderr)
        sys.exit(1)
    print("Scenario 7 passed: Incorrect reset code => 'Reset failed.' error displayed.")

    # --- Scenario 8: Reset form with correct code => "Reset successful."
    # For a real test, you'd have to retrieve the actual code from your app or mock the back-end. 
    # We'll assume "CORRECTCODE" is valid.
    fill_reset_form(browser, "testforgot@example.com", "someUser", "NewPass123", "CORRECTCODE")
    time.sleep(1)
    # Expect "Reset successful." in page
    if not check_in_page_source(browser, "Reset successful."):
        print("Scenario 8 failed: Did not see 'Reset successful.' message with correct code", file=sys.stderr)
        sys.exit(1)
    print("Scenario 8 passed: Correct code => 'Reset successful.' message displayed.")

def main():
    import selenium.webdriver as webDriver
    from selenium.webdriver.chrome.options import Options as chromeOptions
    from selenium.webdriver.firefox.options import Options as firefoxOptions
    from selenium.webdriver.edge.options import Options as edgeOptions
    from selenium.webdriver.safari.options import Options as safariOptions

    if len(sys.argv) != 3:
        print("Usage: python3 forgotPasswordTests.py <browser> <base_url>", file=sys.stderr)
        sys.exit(1)
    
    browser_type = sys.argv[1]
    base_url = sys.argv[2]

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
        print("Invalid browser specified", file=sys.stderr)
        sys.exit(1)
    
    try:
        test_forgot_password_scenarios(browser, base_url)
        print("ALL FORGOT PASSWORD SCENARIOS PASSED")
    except Exception as e:
        print("TEST FAILURE:", e, file=sys.stderr)
        sys.exit(1)
    finally:
        time.sleep(2)  # For observation
        browser.quit()

if __name__ == "__main__":
    main()
