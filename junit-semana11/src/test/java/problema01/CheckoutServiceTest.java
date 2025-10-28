package problema01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.example.checkout.CheckoutResult;
import org.example.checkout.CheckoutService;
import org.example.checkout.CouponService;
import org.example.checkout.CustomerTier;
import org.example.checkout.Item;
import org.example.checkout.ShippingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckoutServiceTest {

    private CheckoutService checkout;
    private CouponService couponService;
    private ShippingService shippingService;
    private LocalDate today;

    @BeforeEach
    void setup() {
        couponService = new CouponService();
        shippingService = new ShippingService();
        checkout = new CheckoutService(couponService, shippingService);
        today = LocalDate.now();
    }

    // -------------------------
    //  TESTES DE ITENS / VALIDAÇÕES
    // -------------------------
    @Test
    void deveLancarErroParaPrecoNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Item("BOOK", -10.0, 1));
    }

    @Test
    void deveLancarErroParaQuantidadeZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new Item("CLOTHES", 50.0, 0));
    }

    // -------------------------
    //  TESTES DE DESCONTO / TIER / PRIMEIRA COMPRA
    // -------------------------
    @Test
    void deveAplicarDescontoGoldEPrimeiraCompra() {
        List<Item> itens = List.of(new Item("CLOTHES", 100, 1));
        CheckoutResult r = checkout.checkout(itens, CustomerTier.GOLD, true,
                "SUL", 2.0, null, today, null);
        // 10% + 5% = 15% de desconto
        assertEquals(15.0, r.discountValue, 0.01);
        assertEquals(10.2, r.tax, 0.01);
        assertEquals(20.0, r.shipping, 0.01);
        assertEquals(115.2, r.total, 0.01);
    }

    @Test
    void naoDeveDarDescontoPrimeiraCompraSeSubtotalMenorQue50() {
        List<Item> itens = List.of(new Item("CLOTHES", 20, 1));
        CheckoutResult r = checkout.checkout(itens, CustomerTier.GOLD, true,
                "SUL", 1.0, null, today, null);
        assertEquals(2.0, r.discountValue, 0.01); // apenas 10% (tier)
    }

    // -------------------------
    //  TESTES DE CUPOM
    // -------------------------
    @Test
    void deveAplicarCupomDESC10() {
        List<Item> itens = List.of(new Item("CLOTHES", 200, 1));
        CheckoutResult r = checkout.checkout(itens, CustomerTier.BASIC, false,
                "SUDESTE", 3.0, "DESC10", today, null);
        assertEquals(20.0, r.discountValue, 0.01);
        assertEquals(21.6, r.tax, 0.01);
        assertEquals(35.0, r.shipping, 0.01);
        assertEquals(236.6, r.total, 0.01);
    }

    @Test
    void deveAplicarCupomDESC20Valido() {
        List<Item> itens = List.of(new Item("CLOTHES", 150, 1));
        CheckoutResult r = checkout.checkout(itens, CustomerTier.SILVER, false,
                "NORTE", 2.0, "DESC20", today, today.plusDays(2));
        // subtotal=150, 25% de desconto (20+5)
        assertEquals(37.5, r.discountValue, 0.01);
        assertEquals(13.5, r.tax, 0.01);
        assertEquals(30.0, r.shipping);
        assertEquals(156.0, r.total, 0.01);
    }

    @Test
    void deveIgnorarCupomDESC20Expirado() {
        List<Item> itens = List.of(new Item("CLOTHES", 150, 1));
        CheckoutResult r = checkout.checkout(itens, CustomerTier.SILVER, false,
                "NORTE", 2.0, "DESC20", today, today.minusDays(1));
        // Cupom expirado → sem desconto adicional
        assertEquals(7.5, r.discountValue, 0.01); // apenas 5% (tier)
        assertEquals(17.1, r.tax, 0.01);
        assertEquals(30.0, r.shipping);
    }

    @Test
    void deveAplicarCupomFreteGratisComPesoPermitido() {
        List<Item> itens = List.of(new Item("CLOTHES", 50, 1));
        CheckoutResult r = checkout.checkout(itens, CustomerTier.BASIC, false,
                "SUDESTE", 4.0, "FRETEGRATIS", today, null);
        assertEquals(0.0, r.shipping);
    }

    @Test
    void naoDeveAplicarFreteGratisPorPesoExcedido() {
        List<Item> itens = List.of(new Item("CLOTHES", 50, 1));
        CheckoutResult r = checkout.checkout(itens, CustomerTier.BASIC, false,
                "SUDESTE", 6.0, "FRETEGRATIS", today, null);
        assertEquals(50.0, r.shipping);
    }

    @Test
    void deveAplicarTetoDe30PorCentoDeDesconto() {
        List<Item> itens = List.of(new Item("CLOTHES", 100, 2));
        CheckoutResult r = checkout.checkout(itens, CustomerTier.GOLD, true,
                "SUL", 2.0, "DESC20", today, today.plusDays(5));
        double descontoPercentual = (r.discountValue / r.subtotal);
        assertEquals(0.30, descontoPercentual, 0.01);
    }

    // -------------------------
    //  TESTES DE FRETE
    // -------------------------
    @Test
    void deveDarFreteGratisPorSubtotalMaiorQue300() {
        List<Item> itens = List.of(new Item("CLOTHES", 150, 2));
        CheckoutResult r = checkout.checkout(itens, CustomerTier.BASIC, false,
                "SUL", 3.0, null, today, null);
        assertEquals(0.0, r.shipping);
    }

    @Test
    void deveCalcularFretePorRegiaoENorte() {
        ShippingService s = new ShippingService();
        assertEquals(30.0, s.calculate("NORTE", 2.0, 50.0, false));
        assertEquals(55.0, s.calculate("NORTE", 3.0, 50.0, false));
        assertEquals(80.0, s.calculate("NORTE", 6.0, 50.0, false));
    }

    @Test
    void deveLancarErroParaPesoNegativo() {
        ShippingService s = new ShippingService();
        assertThrows(IllegalArgumentException.class, () ->
                s.calculate("SUL", -2.0, 100.0, false));
    }
}
