package maia.dmt.market.data.repository

import kotlinx.serialization.json.Json
import maia.dmt.market.data.mapper.ProductMapper
import maia.dmt.market.data.model.ProductsResponse
import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.model.MarketProduct
import maia.dmt.market.domain.repository.MarketRepository

class MarketRepositoryImpl : MarketRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // Static JSON data for now - replace with actual API call later
    private val staticJsonData = """
    {
      "products": [
        {
          "id": 1,
          "name": "cogTest_market_item_white_cheese_500",
          "isDonation": false,
          "price": 0.0,
          "inStock": true,
          "category": "dairy",
          "amount": 0,
          "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_white_cheese.png"
        }
      ]
    }
    """.trimIndent()

    private var cachedProducts: List<MarketProduct> = emptyList()

    private val categories = listOf(
        MarketCategory(
            id = "frozen",
            nameResId = "cogTest_market_category_frozen",
            iconResId = "market_frozen_icon"
        ),
        MarketCategory(
            id = "dairy",
            nameResId = "cogTest_market_category_dairy",
            iconResId = "market_dairy_icon"
        ),
        MarketCategory(
            id = "fruits",
            nameResId = "cogTest_market_category_fruits",
            iconResId = "market_fruits_icon"
        ),
        MarketCategory(
            id = "dry_spices",
            nameResId = "cogTest_market_category_dry_spices",
            iconResId = "market_dry_spices_icon"
        ),
        MarketCategory(
            id = "vegetables",
            nameResId = "cogTest_market_category_vegetables",
            iconResId = "market_vegetables_icon"
        ),
        MarketCategory(
            id = "bakery",
            nameResId = "cogTest_market_category_bakery",
            iconResId = "market_bakery_icon"
        ),
        MarketCategory(
            id = "meat",
            nameResId = "cogTest_market_category_meat",
            iconResId = "market_meat_icon"
        ),
        MarketCategory(
            id = "cleaning_disposable",
            nameResId = "cogTest_market_category_cleaning_disposable",
            iconResId = "market_cleaning_icon"
        )
    )

    override suspend fun getAllCategories(): List<MarketCategory> = categories

    override suspend fun getProductsByCategory(categoryId: String): List<MarketProduct> {
        if (cachedProducts.isEmpty()) {
            cachedProducts = fetchProductsFromApi()
        }
        return cachedProducts.filter { it.categoryId == categoryId }
    }

    override suspend fun getCategoryById(categoryId: String): MarketCategory? {
        return categories.firstOrNull { it.id == categoryId }
    }

    override suspend fun getProductById(productId: String): MarketProduct? {
        if (cachedProducts.isEmpty()) {
            cachedProducts = fetchProductsFromApi()
        }
        return cachedProducts.firstOrNull { it.id == productId }
    }

    override suspend fun getAllProducts(): List<MarketProduct> {
        if (cachedProducts.isEmpty()) {
            cachedProducts = fetchProductsFromApi()
        }
        return cachedProducts
    }

    override suspend fun fetchProductsFromApi(): List<MarketProduct> {
        return try {
            // For now, parse the static JSON
            // TODO: Replace with actual API call using Ktor
            val response = json.decodeFromString<ProductsResponse>(getStaticProductsJson())
            ProductMapper.toDomainList(response.products)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // This method contains the full static JSON data
    // In production, this will be replaced with an actual API call
    private fun getStaticProductsJson(): String {
        return """
{
  "products": [
    {
      "id": 1,
      "name": "cogTest_market_item_white_cheese_500",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dairy",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_white_cheese.png"
    },
    {
      "id": 2,
      "name": "cogTest_market_item_white_cheese_250",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dairy",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_white_cheese.png"
    },
    {
      "id": 3,
      "name": "cogTest_market_item_milk_1_liter_1_percent",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dairy",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_milk1precent.png"
    },
    {
      "id": 4,
      "name": "cogTest_market_item_milk_1_liter_3_percent",
      "isDonation": true,
      "price": 0.0,
      "inStock": true,
      "category": "dairy",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_milk3precent.png"
    },
    {
      "id": 5,
      "name": "cogTest_market_item_sweet_cream_38",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dairy",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_milk_sweet_shamenet.png"
    },
    {
      "id": 6,
      "name": "cogTest_market_item_bulgarian_250",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dairy",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_bulgarian_cheese.png"
    },
    {
      "id": 7,
      "name": "cogTest_market_item_yellow_sliced_250",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dairy",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_yellow_cheese.png"
    },
    {
      "id": 8,
      "name": "cogTest_market_item_yellow_grated_500",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dairy",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_yellowcheese_megurad.png"
    },
    {
      "id": 9,
      "name": "cogTest_market_item_shnitzel_kg",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "meat",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_meat_shnitzel.png"
    },
    {
      "id": 10,
      "name": "cogTest_market_item_legs_4",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "meat",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_meat_chickenshok.png"
    },
    {
      "id": 11,
      "name": "cogTest_market_item_breast_kg",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "meat",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_meat_chickenbreast.png"
    },
    {
      "id": 12,
      "name": "cogTest_market_item_pargit",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "meat",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_meat_pargit.png"
    },
    {
      "id": 13,
      "name": "cogTest_market_item_beef_ground_500",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "meat",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_meat_tahun.png"
    },
    {
      "id": 14,
      "name": "cogTest_market_item_tomato_half_kg",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "vegetables",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_tomato.png"
    },
    {
      "id": 15,
      "name": "cogTest_market_item_cherry_tomato",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "vegetables",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_vegetables_cherrytomato.png"
    },
    {
      "id": 16,
      "name": "cogTest_market_item_cucumber_half_kg",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "vegetables",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_cucumber.png"
    },
    {
      "id": 17,
      "name": "cogTest_market_item_broccoli_fresh_out",
      "isDonation": false,
      "price": 0.0,
      "inStock": false,
      "category": "vegetables",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_broccoli.png"
    },
    {
      "id": 18,
      "name": "cogTest_market_item_carrot_250",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "vegetables",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_vegetables_carrot.png"
    },
    {
      "id": 19,
      "name": "cogTest_market_item_onion",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "vegetables",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_vegetables_onion.png"
    },
    {
      "id": 20,
      "name": "cogTest_market_item_celery",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "vegetables",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_vegetables_cellery.png"
    },
    {
      "id": 21,
      "name": "cogTest_market_item_lemon",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "vegetables",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_lemon.png"
    },
    {
      "id": 22,
      "name": "cogTest_market_item_apricot_pack",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "fruits",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_fruits_mishmish.png"
    },
    {
      "id": 23,
      "name": "cogTest_market_item_banana_100g",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "fruits",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_fruits_banana.png"
    },
    {
      "id": 24,
      "name": "cogTest_market_item_nectarine",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "fruits",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_fruits_peach.png"
    },
    {
      "id": 25,
      "name": "cogTest_market_item_green_apple",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "fruits",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_fruits_greenapple.png"
    },
    {
      "id": 26,
      "name": "cogTest_market_item_red_apple",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "fruits",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_fruits_redapple.png"
    },
    {
      "id": 27,
      "name": "cogTest_market_item_melon",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "fruits",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_fruits_melon.png"
    },
    {
      "id": 28,
      "name": "cogTest_market_item_grapes_500",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "fruits",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_fruits_grapes.png"
    },
    {
      "id": 29,
      "name": "cogTest_market_item_broccoli_frozen",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "frozen",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_brocoli.png"
    },
    {
      "id": 30,
      "name": "cogTest_market_item_peas_frozen",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "frozen",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_afuna.png"
    },
    {
      "id": 31,
      "name": "cogTest_market_item_pea_carrot_mix",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "frozen",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_carrotafuna.png"
    },
    {
      "id": 32,
      "name": "cogTest_market_item_cauliflower_frozen",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "frozen",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_kruvit.png"
    },
    {
      "id": 33,
      "name": "cogTest_market_item_green_beans_frozen",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "frozen",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_beans.png"
    },
    {
      "id": 34,
      "name": "cogTest_market_item_borekas_cheese",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "frozen",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_borekascheese.png"
    },
    {
      "id": 35,
      "name": "cogTest_market_item_borekas_potato",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "frozen",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_borekastapha.png"
    },
    {
      "id": 36,
      "name": "cogTest_market_item_jahnun",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "frozen",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_jahnun.png"
    },
    {
      "id": 37,
      "name": "cogTest_market_item_canned_corn",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_cannedcorn.png"
    },
    {
      "id": 38,
      "name": "cogTest_market_item_pickles_vinegar_250",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_pickle.png"
    },
    {
      "id": 39,
      "name": "cogTest_market_item_pickles_salt_250",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_pickle.png"
    },
    {
      "id": 40,
      "name": "cogTest_market_item_tuna_can",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_tuna.png"
    },
    {
      "id": 41,
      "name": "cogTest_market_item_baby_corn",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_littlecorn.png"
    },
    {
      "id": 42,
      "name": "cogTest_market_item_sardines",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_sardine.png"
    },
    {
      "id": 43,
      "name": "cogTest_market_item_sunflower_oil",
      "isDonation": true,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_oilhamania.png"
    },
    {
      "id": 44,
      "name": "cogTest_market_item_soy_oil",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_oilsoya.png"
    },
    {
      "id": 45,
      "name": "cogTest_market_item_coconut_oil",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_oilcoconut.png"
    },
    {
      "id": 46,
      "name": "cogTest_market_item_olives_canned",
      "isDonation": true,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_olives.png"
    },
    {
      "id": 47,
      "name": "cogTest_market_item_salt_basic",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_salt.png"
    },
    {
      "id": 48,
      "name": "cogTest_market_item_sugar_basic",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_suger.png"
    },
    {
      "id": 49,
      "name": "cogTest_market_item_black_pepper_basic",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_black_pepper.png"
    },
    {
      "id": 50,
      "name": "cogTest_market_item_flour_basic",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "cogTest_market_item_flour_basic"
    },
    {
      "id": 51,
      "name": "cogTest_market_item_cacao_basic",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_cacao.png"
    },
    {
      "id": 52,
      "name": "cogTest_market_item_baking_powder_basic",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_baking_powder.png"
    },
    {
      "id": 53,
      "name": "cogTest_market_item_vanilla_extract_basic",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_vanil.png"
    },
    {
      "id": 54,
      "name": "cogTest_market_item_tomato_paste",
      "isDonation": true,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_tomatopaste.png"
    },
    {
      "id": 55,
      "name": "cogTest_market_item_hummus_dry",
      "isDonation": true,
      "price": 0.0,
      "inStock": true,
      "category": "dry_spices",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_dry_hummus.png"
    },
    {
      "id": 56,
      "name": "cogTest_market_item_bread_no_sugar",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "bakery",
      "amount": 0,
      "imageUrl": "cogTest_market_item_bread_no_sugar"
    },
    {
      "id": 57,
      "name": "cogTest_market_item_bread_regular",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "bakery",
      "amount": 0,
      "imageUrl": "cogTest_market_item_bread_regular"
    },
    {
      "id": 58,
      "name": "cogTest_market_item_challah",
      "isDonation": true,
      "price": 0.0,
      "inStock": true,
      "category": "bakery",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_bakery_halla.png"
    },
    {
      "id": 59,
      "name": "cogTest_market_item_cookies_gluten_free",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "bakery",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_bakery_cookies.png"
    },
    {
      "id": 60,
      "name": "cogTest_market_item_yeast_cake",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "bakery",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_bakery_cake.png"
    },
    {
      "id": 61,
      "name": "cogTest_market_item_bleach",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "cleaning_disposable",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_clean_economica.png"
    },
    {
      "id": 62,
      "name": "cogTest_market_item_gloves",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "cleaning_disposable",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_clean_gloves.png"
    },
    {
      "id": 63,
      "name": "cogTest_market_item_dish_soap",
      "isDonation": true,
      "price": 0.0,
      "inStock": true,
      "category": "cleaning_disposable",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_clean_dishsabon.png"
    },
    {
      "id": 64,
      "name": "cogTest_market_item_window_cleaner",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "cleaning_disposable",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_clean_forwindows.png"
    },
    {
      "id": 65,
      "name": "cogTest_market_item_garbage_bags",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "cleaning_disposable",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_clean_garbagebag.png"
    },
    {
      "id": 66,
      "name": "cogTest_market_item_scotch_pack",
      "isDonation": false,
      "price": 0.0,
      "inStock": true,
      "category": "cleaning_disposable",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_clean_scotch.png"
    },
    {
      "id": 67,
      "name": "cogTest_market_item_disposable_cups_pack",
      "isDonation": true,
      "price": 0.0,
      "inStock": true,
      "category": "cleaning_disposable",
      "amount": 0,
      "imageUrl": "https://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_clean_cups.png"
    }
  ]
}
        """.trimIndent()
    }
}