
import selenium.webdriver as webDriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.chrome.options import Options as chromeOptions
from selenium.webdriver.firefox.options import Options as firefoxOptions
from selenium.webdriver.edge.options import Options as edgeOptions
from selenium.webdriver.safari.options import Options as safariOptions
from signinTests import *
import sys
import time




#for windows: 
#
#python.exe .\seleniumTests.py chrome http://localhost:3000
#for linux:
#
#python3 ./seleniumTests.py chrome http://localhost:3000
#
#Remeber to remove added things to db after tests to be able to test again
#
#Runs acceptance testing for the application
#arg 1 = browser to test 
#arg 2 = url to frontend

def main():
    if len(sys.argv) != 3:
        print("Incorrect arguement length use only url",file=sys.stderr,flush=True)
        sys.exit(1) 
    browser = sys.argv[1]

    if browser == "chrome":
        opts = chromeOptions()
        browser = webDriver.Chrome(options=opts)
    elif browser == "firefox":
        opts = firefoxOptions()
        browser = webDriver.Firefox(options=opts)
    elif browser == "edge":
        opts = edgeOptions()
        browser = webDriver.Edge(options=opts)
    elif browser == "safari":
        opts = safariOptions
        browser = webDriver.Safari(options=opts)
    else:
        browser = None
        print("Invalid browser",file=sys.stderr,flush=True)
        sys.exit(1)
    url = sys.argv[2]
    browser.get(url=url)
    testSignin(browser=browser)
    
    print("TESTS COMPLETED WITHOUT ERROR",file=sys.stdout,flush=True)
if __name__ == "__main__":
    main()