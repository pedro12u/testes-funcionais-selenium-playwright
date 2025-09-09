# Projeto de Testes Funcionais – Selenium & Playwright

Este projeto tem como objetivo **projetar, implementar e testar casos funcionais de login/logout** em diferentes plataformas públicas de demonstração, utilizando **Selenium (Python)** e **Playwright (TypeScript)**.

---

## 👥 Participantes do Grupo
- Pedro Toscano  (R.A:  25362292-2)
- Giovanne Leite  (R.A: 25362248-2)
- Pedro Paulo Barbosa Arantes  (R.A: 25362249-2) 

---

## 📌 Plataformas Testadas
1. **SauceDemo** – https://www.saucedemo.com  
   - Usuário: `standard_user`  
   - Senha: `secret_sauce`  

2. **The Internet (Herokuapp)** – https://the-internet.herokuapp.com/login  
   - Usuário: `tomsmith`  
   - Senha: `SuperSecretPassword!`  

3. **Practice Test Automation** – https://practicetestautomation.com/practice-test-login/  
   - Usuário: `student`  
   - Senha: `Password123`  

4. **OrangeHRM Demo** – https://opensource-demo.orangehrmlive.com/  
   - Usuário: `Admin`  
   - Senha: `admin123`  

---

## 🎯 Fluxos de Teste

### Fluxo Válido
1. Acessar página de login  
2. Realizar login com credenciais corretas  
3. Validar que a página interna foi carregada  
4. Realizar logout e validar retorno para a tela de login  

### Fluxo Inválido
1. Acessar página de login  
2. Inserir credenciais inválidas  
3. Validar exibição da mensagem de erro  

---

✅ Resultados Esperados
Todos os fluxos válidos realizam login e logout corretamente.

Todos os fluxos inválidos exibem mensagem de erro apropriada.

Relatórios do Playwright podem ser abertos via npx playwright show-report.

---
