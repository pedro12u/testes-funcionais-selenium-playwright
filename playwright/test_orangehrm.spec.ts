import { test, expect } from "@playwright/test";

test("Fluxo válido - OrangeHRM", async ({ page }) => {
  await page.goto("https://opensource-demo.orangehrmlive.com/");
  await page.fill("input[name='username']", "Admin");
  await page.fill("input[name='password']", "admin123");
  await page.click("button[type='submit']");

  // Aguarda dashboard
  await page.waitForSelector("#app", { timeout: 15000 });
  await expect(page.locator("#app")).toBeVisible();

  // Logout
  await page.click(".oxd-userdropdown-tab");
  await page.click("text=Logout");
  await page.waitForSelector("input[name='username']");
  await expect(page.locator("input[name='username']")).toBeVisible();
});

test("Fluxo inválido - OrangeHRM", async ({ page }) => {
  await page.goto("https://opensource-demo.orangehrmlive.com/");
  await page.fill("input[name='username']", "wrong");
  await page.fill("input[name='password']", "wrong");
  await page.click("button[type='submit']");

  await page.waitForSelector(".oxd-alert-content-text");
  await expect(page.locator(".oxd-alert-content-text")).toContainText("Invalid credentials");
});
