import sys
import time
import subprocess

def run_test(test_script, browser, base_url):
    """
    Runs a test script as a subprocess and captures its output.
    """
    print(f"Running {test_script}...")

    result = subprocess.run(
        ["python3", test_script, browser, base_url],
        capture_output=True,
        text=True
    )

    # Print the standard output
    print(result.stdout)

    # Print and handle errors
    if result.returncode != 0:
        print(f"ERROR in {test_script}:")
        print(result.stderr)
        sys.exit(1)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python3 seleniumTests.py <browser> <base_url>", file=sys.stderr)
        sys.exit(1)

    browser_type = sys.argv[1]
    base_url = sys.argv[2]

    # Define test scripts
    test_scripts = [
        "signupTests.py",
        "signinTests.py",
        "applicantTests.py",
        "recruiterTests.py",
        "forgotPasswordTests.py",
    ]

    # Run each test
    for script in test_scripts:
        run_test(script, browser_type, base_url)
        time.sleep(2)  # Allow a small delay between tests to prevent rate limits or session issues

    print("ALL TESTS COMPLETED SUCCESSFULLY")
