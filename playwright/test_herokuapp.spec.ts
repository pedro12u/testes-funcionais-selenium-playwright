import { test, expect } from "@playwright/test";

test("Fluxo válido - Herokuapp", async ({ page }) => {
  await page.goto("https://the-internet.herokuapp.com/login");
  await page.fill("#username", "tomsmith");
  await page.fill("#password", "SuperSecretPassword!");
  await page.click("button[type='submit']");

  await expect(page.locator("#flash")).toContainText("You logged into a secure area!");
  await page.click("a[href='/logout']");
  await expect(page.locator("button[type='submit']")).toBeVisible();
});

test("Fluxo inválido - Herokuapp", async ({ page }) => {
  await page.goto("https://the-internet.herokuapp.com/login");
  await page.fill("#username", "wrong");
  await page.fill("#password", "wrong");
  await page.click("button[type='submit']");

  await expect(page.locator("#flash")).toContainText("Your username is invalid!");
});
