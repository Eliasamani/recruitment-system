import time
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
import selenium.webdriver as webDriver
import sys
def testSignin(browser):
    signUpBtn = browser.find_element(By.CLASS_NAME,"get-started-btn")
    signUpBtn.click()
    time.sleep(1)
    if (not "/signup" in browser.current_url):
        print("Signup button not working",file=sys.stderr,flush=True)
    firstNameField = browser.find_element(By.NAME,"firstname")
    lastNameField = browser.find_element(By.NAME,"lastname")
    personNumberField = browser.find_element(By.NAME,"personNumber")
    usernmaeField = browser.find_element(By.NAME,"username")
    emailField = browser.find_element(By.NAME,"email")
    passwordField = browser.find_element(By.NAME,"password")
    registerBtn = browser.find_element(By.CSS_SELECTOR, "button[type='submit']")
    firstNameField.send_keys("A")
    lastNameField.send_keys("Test")
    personNumberField.send_keys("12345678-9999")
    usernmaeField.send_keys("TestRegistredUser")
    emailField.send_keys("A.Test@example.com")
    passwordField.send_keys("testpassword")
    time.sleep(1)
    registerBtn.click()
    time.sleep(1)
    if (not "/signin" in browser.current_url):
        print("register button not working",file=sys.stderr,flush=True)
        sys.exit(1)
    signUpBtn = browser.find_element(By.CLASS_NAME,"get-started-btn")
    signUpBtn.click()
    time.sleep(1)
    if (not "/signup" in browser.current_url):
        print("Signup button not working",file=sys.stderr,flush=True)
        sys.exit(1)
#input same data agian
    firstNameField = browser.find_element(By.NAME,"firstname")
    lastNameField = browser.find_element(By.NAME,"lastname")
    personNumberField = browser.find_element(By.NAME,"personNumber")
    usernmaeField = browser.find_element(By.NAME,"username")
    emailField = browser.find_element(By.NAME,"email")
    passwordField = browser.find_element(By.NAME,"password")
    registerBtn = browser.find_element(By.CSS_SELECTOR, "button[type='submit']")
    firstNameField.send_keys("A")
    lastNameField.send_keys("Test")
    personNumberField.send_keys("12345678-9999")
    usernmaeField.send_keys("TestRegistredUser")
    emailField.send_keys("A.Test@example.com")
    passwordField.send_keys("testpassword")
    time.sleep(1)
    registerBtn.click()
    time.sleep(1)
    if(not "Registration failed" in browser.page_source):
        print("Registration did not fail when signing up same user",file=sys.stderr,flush=True)
        sys.exit(1)