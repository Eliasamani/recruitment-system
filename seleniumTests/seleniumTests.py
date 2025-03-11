import sys
import time
import subprocess
from signupTests import testSignupScenarios
from applicantTests import test_applicant_flow
from forgotPasswordTests import forgot_password_tests
from recruiterTests import test_recruiter_flow
from signinTests import test_signin_scenarios
import selenium.webdriver as webDriver
from selenium.webdriver.chrome.options import Options as chromeOptions
from selenium.webdriver.firefox.options import Options as firefoxOptions
from selenium.webdriver.edge.options import Options as edgeOptions
from selenium.webdriver.safari.options import Options as safariOptions






if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python3 applicantFlowTests.py <browser> <base_url>", file=sys.stderr)
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
        forgot_password_tests(browser, base_url)

    except Exception as e:
        print("TEST FAILURE:", e, file=sys.stderr)
        sys.exit(1)
    finally:
        time.sleep(2)
        browser.quit()



    




    print("ALL TESTS COMPLETED SUCCESSFULLY")
