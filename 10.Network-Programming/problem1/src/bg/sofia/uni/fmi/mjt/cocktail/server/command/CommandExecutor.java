package bg.sofia.uni.fmi.mjt.cocktail.server.command;


import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.CocktailStorage;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.DefaultCocktailStorage;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.Collection;
import java.util.HashSet;

import java.util.Set;
import java.util.stream.Collectors;


public class CommandExecutor {

    private static final String STATUS_CREATED_MESSAGE = "{\"status\":\"CREATED\"}";
    private static final String STATUS_EXISTING_COCKTAIL_MESSAGE =
            "{\"status\":\"ERROR\",\"errorMessage\":\"cocktail %s already exists\"}";
    private static final String STATUS_GET_MESSAGE = "{\"status\":\"OK\",\"cocktails\":[%s]}";
    private static final String INGREDIENTS_MESSAGE = ",\"ingredients\":[";
    private static final String INGREDIENT_NAME_MESSAGE = "{\"name\":\"%s\",\"amount\":\"%s\"}";
    private static final String NAME_MESSAGE = "{\"name\":\"%s\"";
    private static final String STATUS_NO_SUCH_COCKTAIL_MESSAGE =
            "{\"status\":\"ERROR\",\"errorMessage\":\"cocktail %s not found\"}";
    private static final char CLOSING_SIGN = '}';
    private static final char CLOSING_INGREDIENTS_SIGN = ']';

    private static final String CREATE = "create";
    private static final String GET_ALL = "get all";
    private static final String GET_BY_NAME = "get by-name";
    private static final String GET_BY_INGREDIENT = "get by-ingredient";

    private final CocktailStorage storage;

    public CommandExecutor() {
        storage = new DefaultCocktailStorage();
    }

    public String execute(Command cmd) {
        return switch (cmd.command()) {
            case CREATE -> create(cmd.arguments());
            case GET_ALL -> getAllCocktails();
            case GET_BY_NAME -> getCocktailByName(cmd.arguments());
            case GET_BY_INGREDIENT -> getCocktailsByIngredient(cmd.arguments());
            default -> "Unknown command";
        };
    }

    private String create(String[] args) {
        Set<Ingredient> ingredients = new HashSet<>();
        String[] tokens;

        for (int i = 1; i < args.length; i++) {
            tokens = args[i].split("=");
            ingredients.add(new Ingredient(tokens[0], tokens[1]));
        }

        try {
            storage.createCocktail(new Cocktail(args[0], ingredients));
        } catch (CocktailAlreadyExistsException e) {
            return String.format(STATUS_EXISTING_COCKTAIL_MESSAGE, args[0]);
        }

        return STATUS_CREATED_MESSAGE;
    }

    private String getAllCocktails() {
        Collection<Cocktail> cocktails = storage.getCocktails();
        StringBuilder result = new StringBuilder("All cocktails: ");

        for (Cocktail cocktail : cocktails) {
            result.append(cocktail.name()).append(" ");
        }

        return result.toString();
    }

    private String getCocktailByName(String[] args) {
        Cocktail cocktail;
        try {

            cocktail = storage.getCocktail(args[0]);

        } catch (CocktailNotFoundException e) {
            return String.format(STATUS_NO_SUCH_COCKTAIL_MESSAGE, args[0]);
        }


        return String.format(NAME_MESSAGE, cocktail.name()) + INGREDIENTS_MESSAGE +
                cocktail.ingredients().stream()
                        .map(i -> String.format(INGREDIENT_NAME_MESSAGE, i.name(), i.amount()))
                        .collect(Collectors.joining(",")) + CLOSING_INGREDIENTS_SIGN + CLOSING_SIGN;

    }


    private String getCocktailsByIngredient(String[] args) {
        Ingredient ingredient = new Ingredient(args[0], "0");
        Collection<Cocktail> cocktails = storage.getCocktailsWithIngredient(args[0]);
        StringBuilder result = new StringBuilder("Cocktails with this ingredient: ");
        for (Cocktail cocktail : cocktails) {
            if (cocktail.ingredients().contains(ingredient)) {
                result.append(cocktail.name()).append(" ");
            }
        }

        return result.toString();
    }
}