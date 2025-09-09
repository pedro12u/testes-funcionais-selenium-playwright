import { test, expect } from "@playwright/test";

test("Fluxo válido - PracticeTestAutomation", async ({ page }) => {
  await page.goto("https://practicetestautomation.com/practice-test-login/");
  await page.fill("#username", "student");
  await page.fill("#password", "Password123");
  await page.click("#submit");

  // Aguarda confirmação
  await page.waitForSelector("h1", { timeout: 10000 });
  await expect(page.locator("h1")).toContainText("Logged In Successfully");

  // Logout
  await page.click("text=Log out");
  await page.waitForSelector("#submit");
  await expect(page.locator("#submit")).toBeVisible();
});

test("Fluxo inválido - PracticeTestAutomation", async ({ page }) => {
  await page.goto("https://practicetestautomation.com/practice-test-login/");
  await page.fill("#username", "wrong");
  await page.fill("#password", "wrong");
  await page.click("#submit");

  await page.waitForSelector("#error");
  await expect(page.locator("#error")).toContainText("Your password is invalid!");
});
