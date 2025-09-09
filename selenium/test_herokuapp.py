from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

driver = webdriver.Chrome()
wait = WebDriverWait(driver, 10)

# --- Fluxo Válido ---
driver.get("https://the-internet.herokuapp.com/login")
driver.find_element(By.ID, "username").send_keys("tomsmith")
driver.find_element(By.ID, "password").send_keys("SuperSecretPassword!")
driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()

message = wait.until(EC.presence_of_element_located((By.ID, "flash")))
assert "You logged into a secure area!" in message.text
print("✅ Login válido - Herokuapp")

# Logout
driver.find_element(By.CSS_SELECTOR, ".icon-2x.icon-signout").click()
assert wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "button[type='submit']")))
print("✅ Logout realizado")

# --- Fluxo Inválido ---
driver.get("https://the-internet.herokuapp.com/login")
driver.find_element(By.ID, "username").send_keys("wrong")
driver.find_element(By.ID, "password").send_keys("wrong")
driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()

message = wait.until(EC.presence_of_element_located((By.ID, "flash")))
assert "Your username is invalid!" in message.text
print("✅ Erro exibido para login inválido")

driver.quit()
