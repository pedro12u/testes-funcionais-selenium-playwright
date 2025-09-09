from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

driver = webdriver.Chrome()
wait = WebDriverWait(driver, 10)

# --- Fluxo Válido ---
driver.get("https://www.saucedemo.com/")
driver.find_element(By.ID, "user-name").send_keys("standard_user")
driver.find_element(By.ID, "password").send_keys("secret_sauce")
driver.find_element(By.ID, "login-button").click()

assert wait.until(EC.presence_of_element_located((By.ID, "inventory_container")))
print("✅ Login válido - SauceDemo")

# Logout
driver.find_element(By.ID, "react-burger-menu-btn").click()
wait.until(EC.presence_of_element_located((By.ID, "logout_sidebar_link"))).click()
assert wait.until(EC.presence_of_element_located((By.ID, "login-button")))
print("✅ Logout realizado")

# --- Fluxo Inválido ---
driver.get("https://www.saucedemo.com/")
driver.find_element(By.ID, "user-name").send_keys("wrong_user")
driver.find_element(By.ID, "password").send_keys("wrong_pass")
driver.find_element(By.ID, "login-button").click()

error = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "h3[data-test='error']")))
assert "Epic sadface" in error.text
print("✅ Erro exibido para login inválido")

driver.quit()
