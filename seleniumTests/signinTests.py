import time
import sys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def go_to_signin(browser, base_url):
    """
    Navigates to the signin page and waits until '/signin' is in the URL.
    """
    browser.get(base_url + "/signin")
    WebDriverWait(browser, 10).until(lambda b: "/signin" in b.current_url)
    time.sleep(1)  # Allow extra time for page animations if needed

def fill_signin_form(browser, username, password):
    """
    Fills in the signin form fields with the given username and password.
    """
    username_field = WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    password_field = browser.find_element(By.NAME, "password")
    username_field.clear()
    password_field.clear()
    username_field.send_keys(username)
    password_field.send_keys(password)

def click_signin(browser):
    """
    Clicks the signin button and waits a moment for any transitions.
    """
    signin_button = WebDriverWait(browser, 10).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, "button[type='submit']"))
    )
    signin_button.click()
    time.sleep(1)

def logout_if_present(browser):
    """
    Attempts to locate and click the logout button, then waits until '/signin' is in the URL.
    If the logout button isn't found, this function does nothing (no error thrown).
    """
    try:
        # Wait for the header to appear
        WebDriverWait(browser, 5).until(
            EC.presence_of_element_located((By.TAG_NAME, "header"))
        )
        logout_btn = browser.find_element(By.CLASS_NAME, "logout-btn")
        if logout_btn.is_displayed():
            logout_btn.click()
            WebDriverWait(browser, 5).until(lambda b: "/signin" in b.current_url)
    except:
        # If we can't find the logout button or the user isn't logged in, do nothing.
        pass

def test_signin_scenarios(browser, base_url):
    """
    1. Empty submission → required field errors.
    2. Incorrect credentials → 'Login failed' message.
    3. Valid applicant credentials → redirect to /applicant/dashboard → logout → back to /signin.
    4. Valid recruiter credentials → redirect to /recruiter → logout → back to /signin.
    """

    # --- Scenario 1: Empty Submission ---
    go_to_signin(browser, base_url)
    fill_signin_form(browser, "", "")
    click_signin(browser)
    time.sleep(1)
    # Check for a 'required' text or any similar validation error in your page
    if "required" not in browser.page_source.lower():
        print("Scenario 1 failed: No required field errors for empty submission", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 1 passed: Empty submission shows required errors.")

    # --- Scenario 2: Incorrect Credentials ---
    go_to_signin(browser, base_url)
    fill_signin_form(browser, "WrongUser", "WrongPass")
    click_signin(browser)
    time.sleep(1)
    # Expect an error message like "Login failed" (adjust to match your UI)
    if "login failed" not in browser.page_source.lower():
        print("Scenario 2 failed: Incorrect credentials did not show error", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 2 passed: Incorrect credentials show error.")

    # --- Scenario 3: Valid Applicant Credentials + Logout ---
    go_to_signin(browser, base_url)
    fill_signin_form(browser, "TestApplicant", "testpassword")  # Adjust if needed
    click_signin(browser)
    # Wait for applicant dashboard
    WebDriverWait(browser, 10).until(lambda b: "/applicant/dashboard" in b.current_url)
    if "/applicant/dashboard" not in browser.current_url:
        print("Scenario 3 failed: Applicant login did not redirect to applicant dashboard", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 3 partial pass: Applicant credentials redirected correctly.")

    # Logout and confirm redirect to /signin
    logout_if_present(browser)
    if "/signin" not in browser.current_url:
        print("Scenario 3 failed: Did not return to /signin after logout", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 3 passed: Applicant credentials + logout successful.")

    # --- Scenario 4: Valid Recruiter Credentials + Logout ---
    go_to_signin(browser, base_url)
    fill_signin_form(browser, "TestRecruiter", "testpassword")  # Adjust if needed
    click_signin(browser)
    # Wait for recruiter dashboard
    WebDriverWait(browser, 10).until(lambda b: "/recruiter" in b.current_url)
    if "/recruiter" not in browser.current_url:
        print("Scenario 4 failed: Recruiter login did not redirect to recruiter dashboard", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 4 partial pass: Recruiter credentials redirected correctly.")

    # Logout and confirm redirect to /signin
    logout_if_present(browser)
    if "/signin" not in browser.current_url:
        print("Scenario 4 failed: Did not return to /signin after logout", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 4 passed: Recruiter credentials + logout successful.")

if __name__ == "__main__":
    import selenium.webdriver as webDriver
    from selenium.webdriver.chrome.options import Options as chromeOptions
    from selenium.webdriver.firefox.options import Options as firefoxOptions
    from selenium.webdriver.edge.options import Options as edgeOptions
    from selenium.webdriver.safari.options import Options as safariOptions

    if len(sys.argv) != 3:
        print("Usage: python3 signinTests.py <browser> <base_url>", file=sys.stderr, flush=True)
        sys.exit(1)
    
    browser_type = sys.argv[1]
    base_url = sys.argv[2]
    
    # Initialize the webdriver
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
    
    try:
        test_signin_scenarios(browser, base_url)
        print("ALL SIGNIN TEST SCENARIOS PASSED", flush=True)
    except Exception as e:
        print("TEST FAILURE:", e, file=sys.stderr, flush=True)
        sys.exit(1)
    finally:
        time.sleep(2)
        browser.quit()
