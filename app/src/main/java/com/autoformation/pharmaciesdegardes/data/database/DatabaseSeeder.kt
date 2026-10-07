package com.autoformation.pharmaciesdegardes.data.database

import com.autoformation.pharmaciesdegardes.data.model.Medicament
import com.autoformation.pharmaciesdegardes.data.model.Pharmacie
import com.autoformation.pharmaciesdegardes.data.model.Stock

object DatabaseSeeder {

    suspend fun seedDatabase(database: AppDatabase) {
        val pharmacieDao = database.pharmacieDao()
        val medicamentDao = database.medicamentDao()
        val stockDao = database.stockDao()

        // 1. Pharmacies de démonstration
        val pharmacies = listOf(
            Pharmacie(nom = "Pharmacie Centrale", commune = "Kaloum", quartier = "Centre-ville", telephone = "620 00 00 01", latitude = 9.5092, longitude = -13.7122, estDeGarde = true, debutGarde = "08:00", finGarde = "08:00 (J+1)"),
            Pharmacie(nom = "Pharmacie Bon Secours", commune = "Dixinn", quartier = "Bellevue", telephone = "620 00 00 02", latitude = 9.5350, longitude = -13.6820, estDeGarde = false),
            Pharmacie(nom = "Pharmacie de la Paix", commune = "Matam", quartier = "Madina", telephone = "620 00 00 03", latitude = 9.5480, longitude = -13.6500, estDeGarde = true, debutGarde = "18:00", finGarde = "08:00 (J+1)"),
            Pharmacie(nom = "Pharmacie Kaloum", commune = "Kaloum", quartier = "Almamya", telephone = "620 00 00 04", latitude = 9.5130, longitude = -13.7100, estDeGarde = false),
            Pharmacie(nom = "Pharmacie Espoir", commune = "Ratoma", quartier = "Kipé", telephone = "620 00 00 05", latitude = 9.5900, longitude = -13.6300, estDeGarde = false),
            Pharmacie(nom = "Pharmacie Matoto", commune = "Matoto", quartier = "Marché", telephone = "620 00 00 06", latitude = 9.5700, longitude = -13.5800, estDeGarde = true, debutGarde = "08:00", finGarde = "08:00 (J+1)"),
            Pharmacie(nom = "Pharmacie Aéroport", commune = "Matoto", quartier = "Gbessia", telephone = "620 00 00 07", latitude = 9.5630, longitude = -13.6120, estDeGarde = false),
            Pharmacie(nom = "Pharmacie Zn", commune = "Dixinn", quartier = "Cameroun", telephone = "620 00 00 08", latitude = 9.5290, longitude = -13.6930, estDeGarde = true, debutGarde = "18:00", finGarde = "08:00 (J+1)"),
            Pharmacie(nom = "Pharmacie Sans Frontière", commune = "Matam", quartier = "Bonfi", telephone = "620 00 00 09", latitude = 9.5580, longitude = -13.6300, estDeGarde = false),
            Pharmacie(nom = "Pharmacie Santé Plus", commune = "Ratoma", quartier = "Bambéto", telephone = "620 00 00 10", latitude = 9.5850, longitude = -13.6450, estDeGarde = true, debutGarde = "08:00", finGarde = "08:00 (J+1)")
        )

        val pharmacieIds = mutableListOf<Long>()
        for (pharmacie in pharmacies) {
            pharmacieIds.add(pharmacieDao.insert(pharmacie))
        }

        // 2. Médicaments de démonstration
        val medicaments = listOf(
            Medicament(nom = "Paracétamol 500mg",   categorie = "Antalgique",         description = "Antalgique et antipyrétique",                       prix = 15000.0),
            Medicament(nom = "Ibuprofène 400mg",    categorie = "Anti-inflammatoire", description = "Anti-inflammatoire non stéroïdien",                 prix = 25000.0),
            Medicament(nom = "Amoxicilline 1g",     categorie = "Antibiotique",       description = "Antibiotique de la famille des pénicillines",        prix = 45000.0),
            Medicament(nom = "Spasfon",              categorie = "Antispasmodique",    description = "Antispasmodique pour les douleurs au ventre",        prix = 30000.0),
            Medicament(nom = "Doliprane 1000mg",    categorie = "Antalgique",         description = "Soulage la douleur et abaisse la fièvre",            prix = 20000.0),
            Medicament(nom = "Vitamine C 500mg",    categorie = "Supplément",         description = "Supplément vitaminique contre la fatigue",           prix = 10000.0),
            Medicament(nom = "Oméprazole 20mg",     categorie = "Gastro-entérologie", description = "Traitement du reflux gastro-œsophagien",             prix = 50000.0),
            Medicament(nom = "Aspirine 500mg",      categorie = "Antalgique",         description = "Analgésique et anti-inflammatoire",                  prix = 12000.0),
            Medicament(nom = "Cetirizine 10mg",     categorie = "Antihistaminique",   description = "Antihistaminique pour les allergies",                prix = 22000.0),
            Medicament(nom = "Sirop Antitussif",    categorie = "Respiratoire",       description = "Soulage la toux sèche",                              prix = 35000.0),
            Medicament(nom = "Azithromycine 500mg", categorie = "Antibiotique",       description = "Antibiotique macrolide",                             prix = 60000.0),
            Medicament(nom = "Ciprofloxacine 500mg",categorie = "Antibiotique",       description = "Antibiotique de la famille des fluoroquinolones",    prix = 55000.0),
            Medicament(nom = "Artéméther",          categorie = "Antipaludique",      description = "Traitement des accès palustres",                     prix = 40000.0),
            Medicament(nom = "Quinine 300mg",       categorie = "Antipaludique",      description = "Traitement antipaludique traditionnel",              prix = 25000.0),
            Medicament(nom = "Smecta",              categorie = "Anti-diarrhéique",   description = "Pansement digestif",                                 prix = 45000.0),
            Medicament(nom = "Bétadine",            categorie = "Antiseptique",       description = "Solution antiseptique locale",                       prix = 30000.0),
            Medicament(nom = "Thermomètre",         categorie = "Matériel",           description = "Thermomètre digital",                                prix = 25000.0),
            Medicament(nom = "Gants stériles",      categorie = "Matériel",           description = "Paire de gants stériles à usage unique",             prix = 5000.0)
        )

        val medicamentIds = mutableListOf<Long>()
        for (medicament in medicaments) {
            medicamentIds.add(medicamentDao.insert(medicament))
        }

        // 3. Stocks de démonstration 
        val stocks = mutableListOf<Stock>()
        
        for (i in pharmacieIds.indices) {
            val pId = pharmacieIds[i]
            for (j in medicamentIds.indices) {
                // Rendre disponible ~66% du temps pour avoir une liste variée
                val isDisponible = (i + j) % 3 != 0 
                // Ajouter environ la moitié des médicaments à chaque pharmacie de façon semi-aléatoire
                if ((i * j) % 2 == 0 || j % 3 == 0) {
                    stocks.add(Stock(pharmacieId = pId, medicamentId = medicamentIds[j], disponible = isDisponible))
                }
            }
        }

        for (stock in stocks) {
            stockDao.insert(stock)
        }
    }
}
