/*
 * Copyright the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package thepublictransport.schildbach.pte;

import okhttp3.HttpUrl;
import thepublictransport.schildbach.pte.dto.Product;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provider implementation for BART (California)
 * 
 * @author Tristan Marsell
 *
 */
public final class BartProvider extends AbstractHafasClientInterfaceProvider {
    private static final HttpUrl API_BASE = HttpUrl.parse("https://planner.bart.gov/bin");
    private static final Product[] PRODUCTS_MAP = { Product.HIGH_SPEED_TRAIN,
            Product.HIGH_SPEED_TRAIN,
            Product.HIGH_SPEED_TRAIN,
            Product.REGIONAL_TRAIN,
            Product.SUBURBAN_TRAIN,
            Product.BUS,
            Product.FERRY,
            Product.SUBWAY,
            Product.TRAM,
            Product.ON_DEMAND,
            null, null, null, null };
    private static final String DEFAULT_API_CLIENT = "{\"id\": \"BART\", \"type\": \"WEB\", \"name\": \"webapp\", \"l\": \"vs_webapp\"}";

    public BartProvider(final String apiAuthorization) {
        this(DEFAULT_API_CLIENT, apiAuthorization);
    }

    public BartProvider(final String apiClient, final String apiAuthorization) {
        super(NetworkId.BART, API_BASE, PRODUCTS_MAP);
        setApiVersion("1.20");
        setApiClient(apiClient);
        setApiAuthorization(apiAuthorization);
    }

    @Override
    public Set<Product> defaultProducts() {
        return Product.ALL;
    }

    private static final Pattern P_SPLIT_NAME_ONE_COMMA = Pattern.compile("([^,]*), ([^,]*)");

    @Override
    protected String[] splitStationName(final String name) {
        final Matcher m = P_SPLIT_NAME_ONE_COMMA.matcher(name);
        if (m.matches())
            return new String[] { m.group(2), m.group(1) };
        return super.splitStationName(name);
    }

    @Override
    protected String[] splitPOI(final String poi) {
        final Matcher m = P_SPLIT_NAME_FIRST_COMMA.matcher(poi);
        if (m.matches())
            return new String[] { m.group(1), m.group(2) };
        return super.splitStationName(poi);
    }

    @Override
    protected String[] splitAddress(final String address) {
        final Matcher m = P_SPLIT_NAME_FIRST_COMMA.matcher(address);
        if (m.matches())
            return new String[] { m.group(1), m.group(2) };
        return super.splitStationName(address);
    }
}
