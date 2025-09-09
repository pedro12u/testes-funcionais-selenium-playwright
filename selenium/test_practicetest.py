from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

driver = webdriver.Chrome()
wait = WebDriverWait(driver, 10)

# --- Fluxo Válido ---
driver.get("https://practicetestautomation.com/practice-test-login/")
driver.find_element(By.ID, "username").send_keys("student")
driver.find_element(By.ID, "password").send_keys("Password123")
driver.find_element(By.ID, "submit").click()

assert wait.until(EC.presence_of_element_located((By.XPATH, "//h1[contains(text(),'Logged In Successfully')]")))
print("✅ Login válido - PracticeTestAutomation")

# Logout
driver.find_element(By.LINK_TEXT, "Log out").click()
assert wait.until(EC.presence_of_element_located((By.ID, "submit")))
print("✅ Logout realizado")

# --- Fluxo Inválido ---
driver.get("https://practicetestautomation.com/practice-test-login/")
driver.find_element(By.ID, "username").send_keys("wrong")
driver.find_element(By.ID, "password").send_keys("wrong")
driver.find_element(By.ID, "submit").click()

error = wait.until(EC.presence_of_element_located((By.ID, "error")))
assert "Your password is invalid!" in error.text
print("✅ Erro exibido para login inválido")

driver.quit()
