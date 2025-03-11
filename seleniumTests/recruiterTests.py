import time
import sys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


def go_to_signin(browser, base_url):
    """Navigates to the /signin page and waits until it's loaded."""
    browser.get(base_url + "/signin")
    WebDriverWait(browser, 10).until(lambda b: "/signin" in b.current_url)
    time.sleep(1)


def fill_signin_form_and_submit(browser, username, password):
    """Fills in the sign-in form and submits it."""
    username_field = WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((By.NAME, "username"))
    )
    password_field = browser.find_element(By.NAME, "password")
    username_field.clear()
    password_field.clear()
    username_field.send_keys(username)
    password_field.send_keys(password)

    signin_button = WebDriverWait(browser, 10).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, "button[type='submit']"))
    )
    signin_button.click()
    time.sleep(2)


def check_in_page_source(browser, text):
    """Case-insensitive check for 'text' in the page source."""
    return text.lower() in browser.page_source.lower()


def test_recruiter_flow(browser, base_url):
    """
    Scenarios:
    1) Login with recruiter credentials => /recruiter/dashboard
    2) Click "Manage Applications" => wait 10s (the view changes in the same URL)
    3) Approve an application
    4) Reject an application
    5) Show details
    6) Hide details
    """

    # --- Scenario 1: Login => /recruiter/dashboard
    go_to_signin(browser, base_url)
    fill_signin_form_and_submit(browser, "TestRecruiter", "testpassword")  # Adjust if needed

    # Wait for redirect to /recruiter/dashboard
    WebDriverWait(browser, 10).until(lambda b: "/recruiter/dashboard" in b.current_url)
    if "/recruiter/dashboard" not in browser.current_url:
        print("Scenario 1 failed: Not on /recruiter/dashboard after login", file=sys.stderr)
        sys.exit(1)
    print("Scenario 1 passed: Logged in => /recruiter/dashboard")

    # --- Scenario 2: Click on Manage Applications => Wait 10s
    manage_applications_btn = WebDriverWait(browser, 10).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, ".manage-applications-btn"))
    )
    manage_applications_btn.click()
    print("Scenario 2 info: Clicked 'Manage Applications' button.")
    time.sleep(10)  # The view changes in-place, same URL. Enough time for loading.

    print("Scenario 2 passed: Waited 10s after Manage Applications")

    # We'll attempt to find the first row with Approve/Reject/Show details.
    # Because your view re-renders, let's locate them by their classes.

    # Approve & Reject require an application row. We'll attempt to target the first row
    # or the first set of .approve-btn, .reject-btn, .show-details-btn on the page.

    # Scenario 3: Approve an application
    approve_btn = browser.find_element(By.CSS_SELECTOR, ".approve-btn")
    approve_btn.click()
    time.sleep(1)
    print("Scenario 3 passed: Approved an application")

    # Scenario 4: Reject an application
    reject_btn = browser.find_element(By.CSS_SELECTOR, ".reject-btn")
    reject_btn.click()
    time.sleep(1)
    print("Scenario 4 passed: Rejected an application")

    # Scenario 5: Show details
    show_details_btn = browser.find_element(By.CSS_SELECTOR, ".show-details-btn")
    show_details_btn.click()
    time.sleep(1)
    # Optionally check if the text changed to "Hide Details"
    if "Hide Details" not in show_details_btn.text:
        print("Scenario 5 failed: Did not show 'Hide Details' after clicking Show Details", file=sys.stderr)
        sys.exit(1)
    print("Scenario 5 passed: Show more details")

    # Scenario 6: Hide details
    show_details_btn.click()
    time.sleep(1)
    if "Show Details" not in show_details_btn.text:
        print("Scenario 6 failed: Did not revert to 'Show Details' after clicking Hide", file=sys.stderr)
        sys.exit(1)
    print("Scenario 6 passed: Hide details")


if __name__ == "__main__":
    import selenium.webdriver as webDriver
    from selenium.webdriver.chrome.options import Options as chromeOptions
    from selenium.webdriver.firefox.options import Options as firefoxOptions
    from selenium.webdriver.edge.options import Options as edgeOptions
    from selenium.webdriver.safari.options import Options as safariOptions

    if len(sys.argv) != 3:
        print("Usage: python3 recruiterTests.py <browser> <base_url>", file=sys.stderr)
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
        test_recruiter_flow(browser, base_url)
        print("ALL RECRUITER SCENARIOS PASSED")
    except Exception as e:
        print("TEST FAILURE:", e, file=sys.stderr)
        sys.exit(1)
    finally:
        time.sleep(2)
        browser.quit()
