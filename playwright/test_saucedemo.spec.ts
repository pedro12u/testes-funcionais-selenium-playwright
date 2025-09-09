import { test, expect } from "@playwright/test";

test("Fluxo válido - SauceDemo", async ({ page }) => {
  await page.goto("https://www.saucedemo.com/");
  await page.fill("#user-name", "standard_user");
  await page.fill("#password", "secret_sauce");
  await page.click("#login-button");

  // Aguarda até o container do inventário aparecer
  await page.waitForSelector('[data-test="inventory-container"]', { timeout: 15000 });
  await expect(page.locator('[data-test="inventory-container"]')).toBeVisible();

  await page.click("#react-burger-menu-btn");
  await page.click("#logout_sidebar_link");
  await expect(page.locator("#login-button")).toBeVisible();
});

test("Fluxo inválido - SauceDemo", async ({ page }) => {
  await page.goto("https://www.saucedemo.com/");
  await page.fill("#user-name", "wrong");
  await page.fill("#password", "wrong");
  await page.click("#login-button");

  await expect(page.locator("[data-test='error']")).toContainText("Epic sadface");
});
