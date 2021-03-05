package com.gallosalocin.caloriecalculator.data.database

import android.graphics.Color
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gallosalocin.caloriecalculator.models.Category
import com.gallosalocin.caloriecalculator.models.Dish
import com.gallosalocin.caloriecalculator.models.Food
import com.gallosalocin.caloriecalculator.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
        entities = [Food::class, Category::class, Dish::class, User::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(FoodTypeConverter::class)
abstract class CaloriesCalculatorDatabase : RoomDatabase() {

    abstract fun getFoodDao(): FoodDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getDishDao(): DishDao
    abstract fun getUserDao(): UserDao

    // Prepopulate Database
    class Callback @Inject constructor(
        private val database: Provider<CaloriesCalculatorDatabase>,
        private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val categoryDao = database.get().getCategoryDao()
            val foodDao = database.get().getFoodDao()

            applicationScope.launch {
                categoryDao.insertCategory(Category(1, "", Color.parseColor("#FFFFFF")))
                categoryDao.insertCategory(Category(2, "Carbs", Color.parseColor("#FFEA61")))
                categoryDao.insertCategory(Category(3, "Veggies", Color.parseColor("#88B984")))
                categoryDao.insertCategory(Category(4, "Proteins", Color.parseColor("#FF7770")))
                categoryDao.insertCategory(Category(5, "Fruits", Color.parseColor("#CCAAFF")))
                categoryDao.insertCategory(Category(6, "Healthy Fats", Color.parseColor("#A9E5FF")))
                categoryDao.insertCategory(Category(7, "Oil", Color.parseColor("#E1B894")))

                // User Database
//                foodDao.insertFood(Food(id = 1, name = "Chicken Breast Raw", categoryId = 4, calories = 112F, weight = 100, fats = 1.6F, carbs = 0F, prots = 22.7F))
//                foodDao.insertFood(Food(id = 2, name = "Carrot Raw", categoryId = 3, calories = 41F, weight = 100, fats = 0.2F, carbs = 9.6F, prots = 0.9F))
//                foodDao.insertFood(Food(id = 3, name = "Brown Rice Raw", categoryId = 2, calories = 362F, weight = 100, fats = 2.7F, carbs = 76.2F, prots = 7.5F))

                // My Database
                foodDao.insertFood(Food(id = 1, name = "Blanc de poulet", categoryId = 4, calories = 194F, weight = 180, fats = 2.4F, carbs = 0.8F, prots = 42.4F))
                foodDao.insertFood(Food(id = 2, name = "Jambon cuit à l'étouffée", categoryId = 4, calories = 108F, weight = 100, fats = 3F, carbs = .2F, prots = 19F))
                foodDao.insertFood(Food(id = 3, name = "Bar", categoryId = 4, note = "500g - 10mn", calories = 144F, weight = 250, fats = 2.4F, carbs = 0.5F, prots = 30.2F))
                foodDao.insertFood(Food(id = 4, name = "Blanc d'oeuf", categoryId = 4, calories = 19F, weight = 40, fats = 0.1F, carbs = 0.4F, prots = 4.1F))
                foodDao.insertFood(Food(id = 5, name = "Boeuf haché 5%", categoryId = 4, calories = 228F, weight = 175, fats = 8.1F, carbs = 0.5F, prots = 38.3F))
                foodDao.insertFood(Food(id = 6, name = "Fromage blanc carrefour", categoryId = 4, calories = 107F, weight = 150, fats = 4.5F, carbs = 6.8F, prots = 9.8F))
                foodDao.insertFood(Food(id = 7, name = "Lait demi écrémé", categoryId = 4, calories = 46F, weight = 100, fats = 1.6F, carbs = 4.8F, prots = 3.2F))
                foodDao.insertFood(Food(id = 8, name = "Lait écrémé", categoryId = 4, calories = 96F, weight = 300, fats = 0F, carbs = 14.4F, prots = 9.6F))
                foodDao.insertFood(Food(id = 9, name = "Oeuf", categoryId = 4, calories = 67F, weight = 50, fats = 4.3F, carbs = 0.3F, prots = 6.8F))
                foodDao.insertFood(Food(id = 10, name = "Whey protein O.N.", categoryId = 4, calories = 130F, weight = 35, fats = 1.3F, carbs = 2F, prots = 27.5F))
                foodDao.insertFood(Food(id = 11, name = "Whey protein Sci-MX", categoryId = 4, calories = 396F, weight = 100, fats = 6.6F, carbs = 5.9F, prots = 74F))
                foodDao.insertFood(Food(id = 12, name = "Thon entier au naturel", categoryId = 4, calories = 122F, weight = 100, fats = 2F, carbs = 0.1F, prots = 26F))
                foodDao.insertFood(Food(id = 13, name = "Crevettes", categoryId = 4, calories = 113F, weight = 100, fats = 0.6F, carbs = 0.2F, prots = 26.6F))
                foodDao.insertFood(Food(id = 14, name = "Huile d'olive", categoryId = 7, calories = 90F, weight = 10, fats = 10F, carbs = 0F, prots = 0F))
                foodDao.insertFood(Food(id = 15, name = "Beurre de cacahuètes", categoryId = 6, calories = 124F, weight = 20, fats = 10F, carbs = 1.5F, prots = 6.0F))
                foodDao.insertFood(Food(id = 16, name = "Tahini", categoryId = 7, calories = 677F, weight = 100, fats = 62.1F, carbs = 1.3F, prots = 23.7F))
                foodDao.insertFood(Food(id = 17, name = "Olives", categoryId = 7, calories = 233F, weight = 100, fats = 22.6F, carbs = 4.3F, prots = 1.6F))
                foodDao.insertFood(Food(id = 18, name = "Feta", categoryId = 6, calories = 103F, weight = 40, fats = 8.4F, carbs = 0.3F, prots = 6.8F))
                foodDao.insertFood(Food(id = 19, name = "Amandes", categoryId = 6, calories = 634F, weight = 100, fats = 53.4F, carbs = 7.9F, prots = 25.4F))
                foodDao.insertFood(Food(id = 20, name = "Avocat", categoryId = 6, calories = 64F, weight = 40, fats = 13.6F, carbs = 3.4F, prots = 0.8F))
                foodDao.insertFood(Food(id = 21, name = "Buche de chèvre", categoryId = 6, calories = 87F, weight = 30, fats = 6.9F, carbs = 0.4F, prots = 6F))
                foodDao.insertFood(Food(id = 22, name = "Comté", categoryId = 6, calories = 164F, weight = 40, fats = 13.6F, carbs = 0F, prots = 10.4F))
                foodDao.insertFood(Food(id = 23, name = "Chocolat Noir 72%", categoryId = 6, calories = 599F, weight = 100, fats = 46F, carbs = 33F, prots = 8.5F))
                foodDao.insertFood(Food(id = 24, name = "Chocolat Noir 80%", categoryId = 6, calories = 622F, weight = 100, fats = 51F, carbs = 26F, prots = 9.5F))
                foodDao.insertFood(Food(id = 25, name = "Noisettes crues", categoryId = 6, calories = 628F, weight = 100, fats = 61F, carbs = 17F, prots = 15F))
                foodDao.insertFood(Food(id = 26, name = "Noix", categoryId = 6, calories = 42F, weight = 6, fats = 3.8F, carbs = 0.7F, prots = 0.9F))
                foodDao.insertFood(Food(id = 27, name = "Raclette", categoryId = 6, calories = 330F, weight = 100, fats = 26F, carbs = 1F, prots = 23F))
                foodDao.insertFood(Food(id = 28, name = "Coco plats", categoryId = 3, calories = 75F, weight = 250, fats = 0.5F, carbs = 9.1F, prots = 4.6F))
                foodDao.insertFood(Food(id = 29, name = "Asperges mini", categoryId = 3, calories = 9F, weight = 50, fats = 0F, carbs = 0.8F, prots = 0.9F))
                foodDao.insertFood(Food(id = 30, name = "Broccoli", categoryId = 3, calories = 35F, weight = 100, fats = 0.41F, carbs = 7.18F, prots = 2.38F))
                foodDao.insertFood(Food(id = 31, name = "Carotte", categoryId = 3, calories = 34F, weight = 100, fats = 0.4F, carbs = 7.7F, prots = 0.5F))
                foodDao.insertFood(Food(id = 32, name = "Chou fleur", categoryId = 3, calories = 63F, weight = 250, fats = 0.8F, carbs = 12.5F, prots = 4.8F))
                foodDao.insertFood(Food(id = 33, name = "Concombre", categoryId = 3, calories = 14F, weight = 100, fats = 0.1F, carbs = 2F, prots = 0.6F))
                foodDao.insertFood(Food(id = 34, name = "Courgette", categoryId = 3, calories = 17F, weight = 100, fats = 0.32F, carbs = 3.1F, prots = 1.2F))
                foodDao.insertFood(Food(id = 35, name = "Laitue", categoryId = 3, calories = 12F, weight = 80, fats = 0.2F, carbs = 1.0F, prots = 1.0F))
                foodDao.insertFood(Food(id = 36, name = "Oignon", categoryId = 3, calories = 39F, weight = 100, fats = 0.6F, carbs = 6.2F, prots = 1.1F))
                foodDao.insertFood(Food(id = 37, name = "Poivron rouge", categoryId = 3, calories = 29F, weight = 100, fats = 0.35F, carbs = 4.5F, prots = 1.12F))
                foodDao.insertFood(Food(id = 38, name = "Tomate cerise", categoryId = 3, calories = 34F, weight = 160, fats = 0.6F, carbs = 4.8F, prots = 1.3F))
                foodDao.insertFood(Food(id = 39, name = "Pousses de haricot Mungo", categoryId = 3, calories = 22F, weight = 90, fats = 0.4F, carbs = 2.9F, prots = 1.3F))
                foodDao.insertFood(Food(id = 40, name = "Pomme", categoryId = 5, calories = 83F, weight = 160, fats = 0.3F, carbs = 22.4F, prots = 0.5F))
                foodDao.insertFood(Food(id = 41, name = "Ananas", categoryId = 5, calories = 53F, weight = 100, fats = 0.24F, carbs = 11F, prots = 0.52F))
                foodDao.insertFood(Food(id = 42, name = "Abricot", categoryId = 5, calories = 48F, weight = 100, fats = 0.4F, carbs = 11.1F, prots = 1.4F))
                foodDao.insertFood(Food(id = 43, name = "Banane", categoryId = 5, calories = 90F, weight = 100, fats = 0.25F, carbs = 19.6F, prots = 0.98F))
                foodDao.insertFood(Food(id = 44, name = "Cerise", categoryId = 5, calories = 60F, weight = 100, fats = 0.25F, carbs = 11.6F, prots = 1.16F))
                foodDao.insertFood(Food(id = 45, name = "Clémentine", categoryId = 5, calories = 47F, weight = 100, fats = 0.2F, carbs = 12F, prots = 0.9F))
                foodDao.insertFood(Food(id = 46, name = "Datte Medjool", categoryId = 5, calories = 277F, weight = 100, fats = 0.2F, carbs = 75F, prots = 1.8F))
                foodDao.insertFood(Food(id = 47, name = "Nectarine", categoryId = 5, calories = 44F, weight = 100, fats = 0.32F, carbs = 10.55F, prots = 1.06F))
                foodDao.insertFood(Food(id = 48, name = "Peche", categoryId = 5, calories = 39F, weight = 100, fats = 0.33F, carbs = 9F, prots = 1.08F))
                foodDao.insertFood(Food(id = 49, name = "Prune", categoryId = 5, calories = 49F, weight = 100, fats = 0.29F, carbs = 9.41F, prots = 0.66F))
                foodDao.insertFood(Food(id = 50, name = "Raisin jumbo", categoryId = 5, calories = 90F, weight = 30, fats = 0.3F, carbs = 23.7F, prots = 0.9F))
                foodDao.insertFood(Food(id = 51, name = "Glace vanille", categoryId = 2, calories = 190F, weight = 100, fats = 8.7F, carbs = 26F, prots = 2.2F))
                foodDao.insertFood(Food(id = 52, name = "Glace vanille & macadamia", categoryId = 2, calories = 209F, weight = 100, fats = 8.1F, carbs = 31F, prots = 2.7F))
                foodDao.insertFood(Food(id = 53, name = "Glace café", categoryId = 2, calories = 227F, weight = 100, fats = 9.3F, carbs = 32F, prots = 3.5F))
                foodDao.insertFood(Food(id = 54, name = "Bière", categoryId = 2, calories = 200F, weight = 500, fats = 0F, carbs = 18F, prots = 2.5F))
                foodDao.insertFood(Food(id = 55, name = "Biscotte blé complet", categoryId = 2, calories = 408F, weight = 100, fats = 10F, carbs = 66F, prots = 10F))
                foodDao.insertFood(Food(id = 56, name = "Biscotte Crétois", categoryId = 2, calories = 376F, weight = 100, fats = 5.2F, carbs = 69F, prots = 13.4F))
                foodDao.insertFood(Food(id = 57, name = "Pain Burger", categoryId = 2, calories = 231F, weight = 80, fats = 4.7F, carbs = 38.4F, prots = 7.8F))
                foodDao.insertFood(Food(id = 58, name = "Flocons d'avoine", categoryId = 2, calories = 182F, weight = 50, fats = 3.3F, carbs = 29.5F, prots = 6.0F))
                foodDao.insertFood(Food(id = 59, name = "Gaufre au miel", categoryId = 2, calories = 481F, weight = 100, fats = 20F, carbs = 69F, prots = 4.8F))
                foodDao.insertFood(Food(id = 60, name = "Miel", categoryId = 2, calories = 60F, weight = 20, fats = 0F, carbs = 15F, prots = 0.0F))
                foodDao.insertFood(Food(id = 61, name = "Patate", categoryId = 2, calories = 82F, weight = 100, fats = 0.1F, carbs = 18.3F, prots = 1.8F))
                foodDao.insertFood(Food(id = 62, name = "Pates blé complet", categoryId = 2, calories = 216F, weight = 80, fats = 1.2F, carbs = 42F, prots = 7.8F))
                foodDao.insertFood(Food(id = 63, name = "Pita", categoryId = 2, calories = 87F, weight = 35, fats = 0.3F, carbs = 17.5F, prots = 2.9F))
                foodDao.insertFood(Food(id = 64, name = "Pois chiche", categoryId = 2, calories = 329F, weight = 100, fats = 5.6F, carbs = 49.4F, prots = 20.3F))
                foodDao.insertFood(Food(id = 65, name = "Pop corn", categoryId = 2, calories = 365F, weight = 100, fats = 4.5F, carbs = 74F, prots = 9.4F))
                foodDao.insertFood(Food(id = 66, name = "Quinoa tricolore", categoryId = 2, calories = 361F, weight = 100, fats = 6.9F, carbs = 59F, prots = 12F))
                foodDao.insertFood(Food(id = 67, name = "Riz brun", categoryId = 2, calories = 348F, weight = 100, fats = 3.3F, carbs = 70F, prots = 8.2F))
                foodDao.insertFood(Food(id = 68, name = "Sarrasin", categoryId = 2, calories = 212F, weight = 60, fats = 1.3F, carbs = 37.7F, prots = 7.1F))
                foodDao.insertFood(Food(id = 69, name = "Veggie macaroni pois chiche", categoryId = 2, calories = 218F, weight = 60, fats = 3.4F, carbs = 33F, prots = 11.4F))
                foodDao.insertFood(Food(id = 70, name = "Veggie penne lentilles corail", categoryId = 2, calories = 201F, weight = 60, fats = 1F, carbs = 31.2F, prots = 15F))
                foodDao.insertFood(Food(id = 71, name = "Vin rosé", categoryId = 2, calories = 266F, weight = 375, fats = 0F, carbs = 6.8F, prots = 0.8F))
                foodDao.insertFood(Food(id = 72, name = "Cheddar", categoryId = 6, calories = 418F, weight = 100, fats = 35F, carbs = 0.5F, prots = 25F))
                foodDao.insertFood(Food(id = 73, name = "Pain Burger complet", categoryId = 2, calories = 281F, weight = 100, fats = 5.7F, carbs = 45F,prots = 9.5F))
                foodDao.insertFood(Food(id = 74, name = "Glace Peanut Butter B&J", categoryId = 2, calories = 282F, weight = 100, fats = 19F, carbs = 22F, prots = 5.9F))
                foodDao.insertFood(Food(id = 75, name = "Mais en boite", categoryId = 2, calories = 76F, weight = 100, fats = 1.7F, carbs = 11F, prots = 2.8F))
                foodDao.insertFood(Food(id = 76, name = "Cuisse de poulet", categoryId = 4, calories = 162F, weight = 100, fats = 6.2F, carbs = 0F, prots = 26.4F))
                foodDao.insertFood(Food(id = 77, name = "Epinards", categoryId = 3, calories = 24F, weight = 100, fats = 0F, carbs = 0.6F, prots = 3.5F))
                foodDao.insertFood(Food(id = 78, name = "Haricot vert", categoryId = 3, calories = 31F, weight = 100, fats = 0.1F, carbs = 7F, prots = 1.8F))
                foodDao.insertFood(Food(id = 79, name = "Riz risotto", categoryId = 2, calories = 346F, weight = 100, fats = 0.6F, carbs = 77F, prots = 7.7F))
                foodDao.insertFood(Food(id = 80, name = "Kaki", categoryId = 5, calories = 127F, weight = 100, fats = 0.4F, carbs = 34F, prots = 0.8F))
                foodDao.insertFood(Food(id = 81, name = "Chou de Bruxelles", categoryId = 3, calories = 43F, weight = 100, fats = 0.3F, carbs = 9F, prots = 3.4F))
                foodDao.insertFood(Food(id = 82, name = "Navet", categoryId = 3, calories = 28F, weight = 100, fats = 0.1F, carbs = 6F, prots = 0.9F))
                foodDao.insertFood(Food(id = 83, name = "Parmesan", categoryId = 6, calories = 402F, weight = 100, fats = 29.7F, carbs = 0F, prots = 32.4F))
                foodDao.insertFood(Food(id = 84, name = "Omelette", categoryId = 4, calories = 154F, weight = 100, fats = 12F, carbs = 0.6F, prots = 11F))
            }
        }
    }

}