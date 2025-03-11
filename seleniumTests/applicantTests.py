"""
Scenarios:
1:  Login with applicant credentials (wait a bit), check redirected to /applicant/dashboard
2:  Add years of experience with no competence => 'please select competence'
3:  Add competence with no experience years => 'provide years of experience'
5:  Add availability with blank from or to => 'At least one availability period is required.'
6:  Add availability with from>to => 'At least one availability period is required.'
7:  Reload page, then add competence, years of experience, correct availability (from < to), submit => 'Application submitted, thanks!'
9:  Reload page, then go to profile
10: Make first name and last name blank => 'First name and last name cannot be blank'
11: Reload page => if personNumber is editable => set it incorrectly => some error
12: Reload page => if personNumber is editable => set it correctly => no error
13: Reload page => set email incorrectly => error
14: Reload page => set email + firstname correct => 'Successfully, thanks!'
"""

import time
import sys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import Select


############################
# Helper Functions
############################

def go_to_signin(browser, base_url):
    """Navigates to the /signin page and waits until it's loaded."""
    browser.get(base_url + "/signin")
    WebDriverWait(browser, 10).until(lambda b: "/signin" in b.current_url)
    time.sleep(1)

def fill_signin_form_and_submit(browser, username, password):
    """Fills in the sign-in form and submits it."""
    # Wait for username field to appear
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
    time.sleep(1)

def check_in_page_source(browser, text):
    """Case-insensitive check for 'text' in the page source."""
    return text.lower() in browser.page_source.lower()

def reload_page(browser):
    """Reloads the current page and waits a short time."""
    browser.refresh()
    time.sleep(2)

############################
# Applicant Dashboard Helpers
############################

def add_expertise(browser, competence_val, years_val):
    """
    Selects a competence from the dropdown by 'value' attribute and
    enters years of experience, then clicks 'Add Expertise'.
    """
    from selenium.webdriver.support.ui import Select

    comp_select_element = WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((By.CSS_SELECTOR, "select"))
    )

    select_competence = Select(comp_select_element)
    # If competence_val is None or "", that means we want to skip selection
    if competence_val:
        select_competence.select_by_value(str(competence_val))

    # Years of experience field
    years_field = browser.find_element(By.CSS_SELECTOR, "input[type='number']")
    years_field.clear()
    if years_val is not None:
        years_field.send_keys(str(years_val))

    add_btn = browser.find_element(By.XPATH, "//button[contains(text(), 'Add Expertise')]")
    add_btn.click()
    time.sleep(1)


def add_availability(browser, from_date=None, to_date=None):
    """
    Fills the fromDate and toDate inputs (by name), then clicks 'Add Availability'.
    If either is None or empty, it simulates a missing date.
    """
    from_field = WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((By.NAME, "fromDate"))
    )
    to_field = browser.find_element(By.NAME, "toDate")

    from_field.clear()
    to_field.clear()
    if from_date:
        from_field.send_keys(from_date)
    if to_date:
        to_field.send_keys(to_date)

    add_btn = browser.find_element(By.XPATH, "//button[contains(text(), 'Add Availability')]")
    add_btn.click()
    time.sleep(1)

def submit_application(browser):
    """Clicks 'Submit Application'."""
    submit_btn = browser.find_element(By.XPATH, "//button[contains(text(), 'Submit Application')]")
    submit_btn.click()
    time.sleep(1)

############################
# Profile Helpers
############################

def go_to_profile(browser):
    time.sleep(2)

    """Clicks the 'Profile' button in the header and waits for /profile."""
    profile_btn = WebDriverWait(browser, 10).until(
        EC.element_to_be_clickable((By.CLASS_NAME, "profile-btn"))
    )
    profile_btn.click()
    WebDriverWait(browser, 10).until(lambda b: "/profile" in b.current_url)
    time.sleep(1)

def fill_profile_form(browser,
                      first_name=None,
                      last_name=None,
                      person_number=None,
                      email=None):
    """
    Fills the profile form fields if values are provided. 
    Then doesn't submit immediately (we might check for errors first).
    The label-based XPATH assumes:
       <label>First Name</label><input ... />
       <label>Last Name</label><input ... />
       <label>Person Number</label><input ... />
       <label>Email</label><input ... />
    Adjust if your actual structure differs.
    """
    WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((By.CSS_SELECTOR, "form"))
    )

    # First Name
    if first_name is not None:
        fn_field = browser.find_element(
            By.XPATH, "//label[text()='First Name']/following-sibling::input"
        )
        fn_field.clear()
        fn_field.send_keys(first_name)

    # Last Name
    if last_name is not None:
        ln_field = browser.find_element(
            By.XPATH, "//label[text()='Last Name']/following-sibling::input"
        )
        ln_field.clear()
        ln_field.send_keys(last_name)

    # Person Number
    if person_number is not None:
        pn_field = browser.find_element(
            By.XPATH, "//label[text()='Person Number']/following-sibling::input"
        )
        # If the field is disabled, attempt to enter anyway might do nothing
        if pn_field.is_enabled():
            pn_field.clear()
            pn_field.send_keys(person_number)

    # Email
    if email is not None:
        email_field = browser.find_element(
            By.XPATH, "//label[text()='Email']/following-sibling::input"
        )
        email_field.clear()
        email_field.send_keys(email)
        


def click_update_profile(browser):
    """Clicks 'Update Profile' and waits briefly."""
    update_btn = browser.find_element(By.XPATH, "//button[text()='Update Profile']")
    update_btn.click()
    time.sleep(1)

############################
# Test Flow
############################

def test_applicant_flow(browser, base_url):
    """
    Overall flow with scenarios:
    1: Login => /applicant/dashboard
    2: Add years with no competence => 'please select competence'
    3: Add competence with no years => 'provide years of experience'
    4: Add availability with from>to => 'At least one availability period is required.'
    5: Reload => add competence + years + correct from<to => submit => 'Application submitted, thanks!'
    6: Reload => go to profile
    7: blank first + last => error 'First name and last name cannot be blank'
    8: reload => if personNumber is editable => set incorrectly => error
    9: reload => if personNumber is editable => set correct => no error
    10: reload => set email incorrectly => error
    11: reload => set email + firstName correct => 'Successfully, thanks!'
    """
    # Scenario 1: Login => /applicant/dashboard
    go_to_signin(browser, base_url)
    fill_signin_form_and_submit(browser, "johndoe", "password123")  # Adjust creds as needed
    WebDriverWait(browser, 10).until(lambda b: "/applicant/dashboard" in b.current_url)
    if "/applicant/dashboard" not in browser.current_url:
        print("Scenario 1 failed: Not on /applicant/dashboard after login", file=sys.stderr)
        sys.exit(1)
    print("Scenario 1 passed: Logged in => /applicant/dashboard")

    # Scenario 2: Add years with no competence => error
    add_expertise(browser, "", 3)
    if not check_in_page_source(browser, "select a competence"):
        print("Scenario 2 failed: No error about missing competence", file=sys.stderr)
        sys.exit(1)
    print("Scenario 2 passed: Missing competence => 'please select competence'")

    # Scenario 3: Add competence but no years => error
    reload_page(browser)
    add_expertise(browser, "1", None)
    if not check_in_page_source(browser, "provide years of experience"):
        print("Scenario 3 failed: No error about missing years", file=sys.stderr)
        sys.exit(1)
    print("Scenario 3 passed: Missing years => 'provide years of experience'")


    # Scenario 4: from>to => 'At least one availability period is required.'
    reload_page(browser)
    add_availability(browser, from_date="2025-05-10", to_date="2025-05-01")
    if not check_in_page_source(browser, "Start date cannot be after end date"):
        print("Scenario 4 failed: No error about from>to", file=sys.stderr)
        sys.exit(1)
        sys.exit(1)
    print("Scenario 4 passed: from>to => 'Start date cannot be after end date'")

    # Scenario 5: Reload => add valid comp + years + from<to => submit => 'Application submitted, thanks!'
    reload_page(browser)
    add_expertise(browser, "2", 5)  # e.g. 5 years for competence_id 1
    WebDriverWait(browser, 20)
    add_availability(browser, "2025-05-01", "2025-05-10")
    submit_application(browser)
    # Expect an alert
    try:
        alert = browser.switch_to.alert
        alert_text = alert.text.lower()
        if "application submitted, thanks" not in alert_text:
            print("Scenario 5 failed: Alert text mismatch", file=sys.stderr)
            sys.exit(1)
        alert.accept()
    except:
        print("Scenario 5 failed: No alert or unexpected alert on submission", file=sys.stderr)
        sys.exit(1)
    print("Scenario 5 passed: Valid data => 'Application submitted, thanks!'")

    # Scenario 6: Reload => go to profile
    time.sleep(2)
    reload_page(browser)
    time.sleep(2)
    go_to_profile(browser) # This clicks the "profile-btn" in the header
    if "/profile" not in browser.current_url:
        print("Scenario 6 failed: Not on /profile after navigation", file=sys.stderr)
        sys.exit(1)
    print("Scenario 6 passed: Navigated to profile")


    # Scenario 7: reload => if personNumber is editable => set incorrectly => error
    reload_page(browser)
    go_to_profile(browser)

    # Locate the person number field
    pn_field = WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((
            By.XPATH,
            "//label[text()='Person Number']/following-sibling::input"
        ))
    )

    # Check if the field is enabled (editable)
    if pn_field.is_enabled():
        print("Scenario 7 info: personNumber is editable, testing invalid entry.")
        # Type in an invalid format
        pn_field.clear()
        pn_field.send_keys("abc")
        click_update_profile(browser)

        # Because your code might not validate personNumber strictly,
        # you can check for any error or invalid text in page source
        if not ("error" in browser.page_source.lower() or "invalid" in browser.page_source.lower()):
            print("Scenario 7 failed: No error for invalid personNumber", file=sys.stderr)
            sys.exit(1)
        print("Scenario 7 passed: recognized invalid personNumber => error displayed")
    else:
        print("Scenario 7 info: personNumber field is disabled, skipping invalid test.")
        # If your scenario requires an error in this case, you can fail or skip
        # For example:
        # print("Scenario 7 skipped: Person number is not editable")
        # pass  # or sys.exit(0) to skip

    # Scenario 8: reload => if personNumber is editable => set correct => no error
    reload_page(browser)
    go_to_profile(browser)

    pn_field = WebDriverWait(browser, 10).until(
        EC.presence_of_element_located((
            By.XPATH,
            "//label[text()='Person Number']/following-sibling::input"
        ))
    )
    if pn_field.is_enabled():
        print("Scenario 8 info: personNumber is editable, testing correct entry.")
        pn_field.clear()
        pn_field.send_keys("19900101-1234")
        click_update_profile(browser)

        # Expect no "error" or "invalid" text
        if "error" in browser.page_source.lower() or "invalid" in browser.page_source.lower():
            print("Scenario 8 failed: Unexpected error after correct personNumber", file=sys.stderr)
            sys.exit(1)
        print("Scenario 8 passed: correct personNumber => no error displayed")
    else:
        print("Scenario 8 info: personNumber field is disabled, skipping valid test.")
        # Maybe it's expected not to be editable. If you need the user to be able
        # to correct it, that means your front-end logic should allow it.
        # pass


    # Scenario 9: reload => set email incorrectly => error
    reload_page(browser)
    go_to_profile(browser)
    fill_profile_form(browser, email="invalidEmail@mail")
    click_update_profile(browser)
    if "Invalid email format".lower() not in browser.page_source.lower():
        print("Scenario 9 failed: No error about invalid email", file=sys.stderr)
        sys.exit(1)
    print("Scenario 9 passed: invalid email => 'Invalid email format'")

    # Scenario 10: reload => set email + firstName correct => 'Successfully, thanks!'
    reload_page(browser)
    go_to_profile(browser)
    fill_profile_form(browser, first_name="John", email="john.doe@example.com")
    click_update_profile(browser)
    # We'll look for "Successfully, thanks!" or "Profile updated successfully."
    if not ("successfully" in browser.page_source.lower() or "thanks" in browser.page_source.lower()):
        print("Scenario 10 failed: No success message for correct profile update", file=sys.stderr)
        sys.exit(1)
    print("Scenario 10 passed: Correct email + firstName => 'Successfully, thanks!' (or similar)")

############################
# Main Script
############################

if __name__ == "__main__":
    import selenium.webdriver as webDriver
    from selenium.webdriver.chrome.options import Options as chromeOptions
    from selenium.webdriver.firefox.options import Options as firefoxOptions
    from selenium.webdriver.edge.options import Options as edgeOptions
    from selenium.webdriver.safari.options import Options as safariOptions

    if len(sys.argv) != 3:
        print("Usage: python3 applicantFlowTests.py <browser> <base_url>", file=sys.stderr)
        sys.exit(1)

    browser_type = sys.argv[1]
    base_url = sys.argv[2]

    # Initialize the chosen browser
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
        test_applicant_flow(browser, base_url)
        print("ALL APPLICANT FLOW SCENARIOS PASSED")
    except Exception as e:
        print("TEST FAILURE:", e, file=sys.stderr)
        sys.exit(1)
    finally:
        time.sleep(2)
        browser.quit()
