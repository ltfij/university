package com.hjwylde.uni.swen222.assignment02.cluedo.ui.gui;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.Card;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.CharacterCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.RoomCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.cards.game.WeaponCard;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Character;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Room;
import com.hjwylde.uni.swen222.assignment02.cluedo.game.items.Weapon;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * A utility class for initialising and getting images for the GUI.
 * 
 * @author Henry J. Wylde
 * 
 * @since 31/08/2013
 */
public final class Images {

    private static Map<ImageObject, Image> images = new HashMap<>();

    /**
     * This class cannot be instantiated.
     */
    private Images() {}

    /**
     * Gets the associated image for the given image object.
     * 
     * @param obj the image object.
     * @return the associated image.
     */
    public static Image getImage(ImageObject obj) {
        checkState(images.containsKey(obj), "Images have not been properly initialised");

        return images.get(obj);
    }

    /**
     * Gets the associated image for the given card.
     * 
     * @param card the card.
     * @return the associated image.
     */
    public static Image getImageForCard(Card card) {
        if (card instanceof CharacterCard)
            return getImageForCharacter(((CharacterCard) card).getCharacter());
        else if (card instanceof WeaponCard)
            return getImageForWeapon(((WeaponCard) card).getWeapon());
        else if (card instanceof RoomCard)
            return getImageForRoom(((RoomCard) card).getRoom());
        else
            // DEADCODE: Throw internal error
            throw new InternalError("DEADCODE");
    }

    /**
     * Gets the associated image for the character.
     * 
     * @param character the character.
     * @return the associated image.
     */
    public static Image getImageForCharacter(Character character) {
        return getImage(ImageObject.valueOf(character.name()));
    }

    /**
     * Gets the associated image for the room.
     * 
     * @param room the room.
     * @return the associated image.
     */
    public static Image getImageForRoom(Room room) {
        return getImage(ImageObject.valueOf(room.name()));
    }

    /**
     * Gets the associated image for the weapon.
     * 
     * @param weapon the weapon.
     * @return the image.
     */
    public static Image getImageForWeapon(Weapon weapon) {
        return getImage(ImageObject.valueOf(weapon.name()));
    }

    /**
     * Gets all of the associated images for the given image objects.
     * 
     * @param objs the image objects.
     * @return the associated images.
     */
    public static Image[] getImages(ImageObject[] objs) {
        Image[] images = new Image[objs.length];

        for (int i = 0; i < objs.length; i++)
            images[i] = getImage(objs[i]);

        return images;
    }

    /**
     * Gets all of the associated images for the given cards.
     * 
     * @param cards the cards.
     * @return the associated images.
     */
    public static Image[] getImagesForCards(Card[] cards) {
        Image[] images = new Image[cards.length];

        for (int i = 0; i < cards.length; i++)
            images[i] = getImageForCard(cards[i]);

        return images;
    }

    /**
     * Gets all of the associated images for the given cards.
     * 
     * @param cards the cards.
     * @return the associated images.
     */
    public static List<Image> getImagesForCards(List<? extends Card> cards) {
        return Arrays.asList(getImagesForCards(cards.toArray(new Card[0])));
    }

    /**
     * Gets all of the associated images for the given characters.
     * 
     * @param characters the characters.
     * @return the associated images.
     */
    public static Image[] getImagesForCharacters(Character[] characters) {
        Image[] images = new Image[characters.length];

        for (int i = 0; i < characters.length; i++)
            images[i] = getImageForCharacter(characters[i]);

        return images;
    }

    /**
     * Gets all of the associated images for the given characters.
     * 
     * @param characters the characters.
     * @return the associated images.
     */
    public static List<Image> getImagesForCharacters(List<Character> characters) {
        return Arrays.asList(getImagesForCharacters(characters.toArray(new Character[0])));
    }

    /**
     * Gets all of the associated images for the given rooms.
     * 
     * @param rooms the rooms.
     * @return the associated images.
     */
    public static List<Image> getImagesForRooms(List<Room> rooms) {
        return Arrays.asList(getImagesForRooms(rooms.toArray(new Room[0])));
    }

    /**
     * Gets all of the associated images for the given rooms.
     * 
     * @param rooms the rooms.
     * @return the associated images.
     */
    public static Image[] getImagesForRooms(Room[] rooms) {
        Image[] images = new Image[rooms.length];

        for (int i = 0; i < rooms.length; i++)
            images[i] = getImageForRoom(rooms[i]);

        return images;
    }

    /**
     * Gets all of the associated images for the given weapons.
     * 
     * @param weapons the weapons.
     * @return the associated images.
     */
    public static List<Image> getImagesForWeapons(List<Weapon> weapons) {
        return Arrays.asList(getImagesForWeapons(weapons.toArray(new Weapon[0])));
    }

    /**
     * Gets all of the associated images for the given weapons.
     * 
     * @param weapons the weapons.
     * @return the images.
     */
    public static Image[] getImagesForWeapons(Weapon[] weapons) {
        Image[] images = new Image[weapons.length];

        for (int i = 0; i < weapons.length; i++)
            images[i] = getImageForWeapon(weapons[i]);

        return images;
    }

    /**
     * Attempts to initialise all of the images with the default directory.
     * 
     * @throws FileNotFoundException if the images cannot be loaded.
     */
    public static void initialiseImages() throws FileNotFoundException {
        initialiseImages("/assignment02/");
    }

    /**
     * Attempts to initialise all of the images with the given directory.
     * 
     * @param dir the directory.
     * @throws FileNotFoundException if the images cannot be loaded.
     */
    public static void initialiseImages(String dir) throws FileNotFoundException {
        loadImage(ImageObject.BOARD, dir + "board.jpg");

        // Card images

        // Characters
        for (Character character : Character.values())
            loadImage(ImageObject.valueOf(character.name()), dir + "cards/characters/"
                    + character.name().toLowerCase(Locale.ENGLISH).replace("_", "-") + ".jpg");

        // Weapons
        for (Weapon weapon : Weapon.values())
            loadImage(ImageObject.valueOf(weapon.name()), dir + "cards/weapons/"
                    + weapon.name().toLowerCase(Locale.ENGLISH).replace("_", "-") + ".jpg");

        // Rooms
        for (Room room : Room.values())
            loadImage(ImageObject.valueOf(room.name()), dir + "cards/rooms/"
                    + room.name().toLowerCase(Locale.ENGLISH).replace("_", "-") + ".jpg");
    }

    /**
     * Checks whether the images are all initialised. That is, all image objects have an associated
     * image.
     * 
     * @return true if the images are initialised.
     */
    public static boolean isImagesInitialised() {
        for (ImageObject obj : ImageObject.values())
            if (!images.containsKey(obj))
                return false;

        return true;
    }

    /**
     * Attempts to load the image at the given path for the image object.
     * 
     * @param obj the image object.
     * @param path the path.
     * @throws FileNotFoundException if the file cannot be found.
     */
    private static void loadImage(ImageObject obj, String path) throws FileNotFoundException {
        Image img = new ImageIcon(Images.class.getResource(path)).getImage();

        images.put(checkNotNull(obj, "Image object cannot be null"), img);
    }

    /**
     * A list of the different image objects available to the GUI.
     * 
     * @author Henry J. Wylde
     * 
     * @since 31/08/2013
     */
    public static enum ImageObject {
        /**
         * The cluedo board.
         */
        BOARD,
        /**
         * The rope weapon.
         */
        ROPE,
        /**
         * The candlestick weapon.
         */
        CANDLESTICK,
        /**
         * The knife weapon.
         */
        KNIFE,
        /**
         * The pistol weapon.
         */
        PISTOL,
        /**
         * The baseball bat weapon.
         */
        BASEBALL_BAT,
        /**
         * The dumbbell weapon.
         */
        DUMBBELL,
        /**
         * The trophy weapon.
         */
        TROPHY,
        /**
         * The poison weapon.
         */
        POISON,
        /**
         * The axe weapon.
         */
        AXE,
        /**
         * The kitchen room.
         */
        KITCHEN,
        /**
         * The dining room.
         */
        DINING_ROOM,
        /**
         * The guest house room.
         */
        GUEST_HOUSE,
        /**
         * The patio room.
         */
        PATIO,
        /**
         * The pool room.
         */
        POOL,
        /**
         * The hall room.
         */
        HALL,
        /**
         * The observatory room.
         */
        OBSERVATORY,
        /**
         * The living room.
         */
        LIVING_ROOM,
        /**
         * The theatre room.
         */
        THEATRE,
        /**
         * The spa room.
         */
        SPA,
        /**
         * Kasandra Scarlett.
         */
        KASANDRA_SCARLETT,
        /**
         * Jack Mustard.
         */
        JACK_MUSTARD,
        /**
         * Diane White.
         */
        DIANE_WHITE,
        /**
         * Jacob Green.
         */
        JACOB_GREEN,
        /**
         * Eleanor Peacock.
         */
        ELEANOR_PEACOCK,
        /**
         * Victor Plum.
         */
        VICTOR_PLUM;
    }
}
