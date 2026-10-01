# 🏥 Module Pharmacies de Garde (`feature/pharmacies`)

Ce module gère l'affichage, le filtrage et le suivi des pharmacies de garde au sein de l'application **PharmaciesDeGardes**. Il a été développé selon l'architecture **MVVM** et les bonnes pratiques recommandées par Google pour Android (Jetpack Compose).

---

## 🚀 Fonctionnalités intégrées

- **Liste des pharmacies :** Affichage clair des pharmacies disponibles avec leurs informations de contact et leur localisation.
- **Filtrage par commune :** Recherche rapide des pharmacies selon la commune sélectionnée.
- **Filtre de garde (`estDeGarde`) :** Option permettant de n'afficher que les pharmacies actuellement de garde.
- **Gestion des états UI :** Gestion réactive des états (chargement, liste vide, résultat filtré) via Kotlin StateFlow.
- **Écran de détail :** Consultation des détails complets d'une pharmacie sélectionnée.

---

## 🏗️ Architecture et Structure du Code

Le code de ce module est entièrement isolé dans le package `ui/pharmacies/` afin de garantir une modularité propre et d'éviter les conflits lors de la fusion.

```text
app/src/main/java/com/autoformation/pharmaciesdegardes/ui/pharmacies/
├── components/                  # Composants UI réutilisables (cartes, filtres, etc.)
├── PharmacieUiState.kt          # État de l'interface utilisateur (State)
├── PharmacieViewModel.kt        # Logique métier et gestion du filtrage
├── ListePharmaciesScreen.kt     # Écran principal de la liste des pharmacies
└── DetailPharmacieScreen.kt     # Écran de détails d'une pharmacie
