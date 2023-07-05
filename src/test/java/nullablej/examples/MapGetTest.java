//  MIT License
//  
//  Copyright (c) 2017-2023 Nawa Manusitthipol
//  
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//  
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//  SOFTWARE.

package nullablej.examples;

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
import nullablej.NullableJ;

@ExtensionMethod({ NullableJ.class })
public class MapGetTest {
    
    private ItemService itemService          = new ItemService();
    private SaleStateService saleStatService = new SaleStateService();
    
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
