package nullablej;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.reducing;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import lombok.Value;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod({ NullableJ.class })
public class MapGet {
    
    private ItemService itemService          = new ItemService();
    private SaleStateService saleStatService = new SaleStateService();

    @SuppressWarnings("unused")
    private BigDecimal totalMonthlySaleByPart_notNullSafe(String partNumber, Color color, int year) {
        val item        = itemService.findItem(partNumber, color);
        val salesByYear = saleStatService.findItemSalesByYear(item);
        return salesByYear.get(year).stream().map(Sale::getTotal).collect(reducing(ZERO, BigDecimal::add));
    }
    
    private BigDecimal totalMonthlySaleByPart(String partNumber, Color color, int year) {
        val item        = itemService.findItem(partNumber, color);
        val salesByYear = saleStatService.findItemSalesByYear(item);
        return salesByYear._get(year)._stream$().map(Sale::getTotal).collect(reducing(ZERO, BigDecimal::add));
    }
    
    @Test
    public void test() {
        assertEquals(0, totalMonthlySaleByPart(null, null, 0).intValue());
    }
    
    @Value
    public static class Sale {
        private BigDecimal total;
    }
    @Value
    public static class Item {
        private String id;
    }
    
    public static class SaleStateService {
        public Map<Integer, List<Sale>> findItemSalesByYear(Item item) {
            return null;
        }
    }
    public static class ItemService {
        private Item findItem(String partNumber, Color color) {
            return null;
        }
    }
    
    
    
}
