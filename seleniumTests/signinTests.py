import time
import sys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def go_to_signin(browser, base_url):
    """
    Navigates to the signin page.
    
    @param browser: The Selenium webdriver instance.
    @param base_url: The base URL of the front-end.
    """
    browser.get(base_url + "/signin")
    WebDriverWait(browser, 10).until(lambda b: "/signin" in b.current_url)
    time.sleep(1)  # Allow extra time for page animations if needed

def fill_signin_form(browser, username, password):
    """
    Fills in the signin form with the provided username and password.
    
    @param browser: The Selenium webdriver instance.
    @param username: The username to input.
    @param password: The password to input.
    """
    # Wait for the input fields to be present.
    usernameField = WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    passwordField = browser.find_element(By.NAME, "password")
    usernameField.clear()
    passwordField.clear()
    usernameField.send_keys(username)
    passwordField.send_keys(password)

def click_signin(browser):
    """
    Clicks the signin button.
    
    @param browser: The Selenium webdriver instance.
    """
    signinButton = WebDriverWait(browser, 10).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, "button[type='submit']"))
    )
    signinButton.click()
    time.sleep(1)

def testSigninScenarios(browser, base_url):
    """
    Runs several signin scenarios:
    
    1. Empty submission should show required field errors.
    2. Incorrect credentials should display a "Login failed" message.
    3. Valid applicant credentials should redirect to applicant dashboard.
    4. Valid recruiter credentials should redirect to recruiter dashboard.
    
    @param browser: The Selenium webdriver instance.
    @param base_url: The base URL of the front-end.
    """
    # ----- Scenario 1: Empty Form Submission -----
    go_to_signin(browser, base_url)
    fill_signin_form(browser, "", "")
    click_signin(browser)
    time.sleep(1)
    # We assume the page will contain the word "required" in the error messages.
    if "required" not in browser.page_source:
        print("Scenario 1 failed: No required field errors for empty submission", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 1 passed: Empty form submission shows required errors.")
    
    # ----- Scenario 2: Incorrect Credentials -----
    go_to_signin(browser, base_url)
    fill_signin_form(browser, "WrongUser", "WrongPass")
    click_signin(browser)
    time.sleep(1)
    # Expect an error message like "Login failed" (adjust text as per your UI)
    if "Login failed" not in browser.page_source:
        print("Scenario 2 failed: Incorrect credentials did not show error", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 2 passed: Incorrect credentials show error.")
    
    # ----- Scenario 3: Valid Applicant Credentials -----
    go_to_signin(browser, base_url)
    fill_signin_form(browser, "TestApplicant", "testpassword")
    click_signin(browser)
    # Wait until redirected to the applicant dashboard.
    WebDriverWait(browser, 10).until(lambda b: "/applicant/dashboard" in b.current_url)
    if "/applicant/dashboard" not in browser.current_url:
        print("Scenario 3 failed: Applicant credentials did not redirect to applicant dashboard", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 3 passed: Applicant credentials redirected to applicant dashboard.")
    
    # ----- Scenario 4: Valid Recruiter Credentials -----
    go_to_signin(browser, base_url)
    fill_signin_form(browser, "TestRecruiter", "testpassword")
    click_signin(browser)
    # Wait until redirected to the recruiter dashboard.
    WebDriverWait(browser, 10).until(lambda b: "/recruiter" in b.current_url)
    if "/recruiter" not in browser.current_url:
        print("Scenario 4 failed: Recruiter credentials did not redirect to recruiter dashboard", file=sys.stderr, flush=True)
        sys.exit(1)
    print("Scenario 4 passed: Recruiter credentials redirected to recruiter dashboard.")

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
        testSigninScenarios(browser, base_url)
        print("ALL SIGNIN TEST SCENARIOS PASSED", flush=True)
    except Exception as e:
        print("TEST FAILURE:", e, file=sys.stderr, flush=True)
        sys.exit(1)
    finally:
        time.sleep(2)
        browser.quit()
