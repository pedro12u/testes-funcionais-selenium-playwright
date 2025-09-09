from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

driver = webdriver.Chrome()
wait = WebDriverWait(driver, 10)

# --- Fluxo Válido ---
driver.get("https://opensource-demo.orangehrmlive.com/")
driver.find_element(By.NAME, "username").send_keys("Admin")
driver.find_element(By.NAME, "password").send_keys("admin123")
driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()

assert wait.until(EC.presence_of_element_located((By.ID, "app")))
print("✅ Login válido - OrangeHRM")

# Logout
driver.find_element(By.CLASS_NAME, "oxd-userdropdown-tab").click()
wait.until(EC.presence_of_element_located((By.LINK_TEXT, "Logout"))).click()
assert wait.until(EC.presence_of_element_located((By.NAME, "username")))
print("✅ Logout realizado")

# --- Fluxo Inválido ---
driver.get("https://opensource-demo.orangehrmlive.com/")
driver.find_element(By.NAME, "username").send_keys("wrong")
driver.find_element(By.NAME, "password").send_keys("wrong")
driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()

error = wait.until(EC.presence_of_element_located((By.CLASS_NAME, "oxd-alert-content-text")))
assert "Invalid credentials" in error.text
print("✅ Erro exibido para login inválido")

driver.quit()
