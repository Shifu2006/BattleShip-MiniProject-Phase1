import java.util.Scanner;
import java.util.Random;

/**
  The BattleShip class manages the gameplay of the Battleship game between two players.
  It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {
    // ha ha ha ha
    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
      The main method that runs the game loop.
      It initializes the grids for both players, places ships randomly, and manages turns.
      The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    /**
      Initializes the grid by filling it with water ('~').

      @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = '~';
            }
        }
    }

    /**
      Places ships randomly on the given grid.
      This method is called for both players to place their ships on their respective grids.

      @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        Random rand = new Random();
        for(int i = 5; i >= 2; i--){
            int rInt = rand.nextInt(10);
            int cInt = rand.nextInt(10);
            boolean horizontal = rand.nextBoolean();
            while(!canPlaceShip(grid, rInt, cInt, i, horizontal)){
                rInt = rand.nextInt(10);
                cInt = rand.nextInt(10);
                horizontal = rand.nextBoolean();
            }
            if(horizontal){
                for(int j = 0; j < i; j++){
                    grid[rInt][cInt + j] = 'S';
                }
            } else {
                for(int j = 0; j < i; j++){
                    grid[rInt + j][cInt] = 'S';
                }
            }
        }
    }

    /**
      Checks if a ship can be placed at the specified location on the grid.
      This includes checking the size of the ship, its direction (horizontal or vertical),
      and if there's enough space to place it.

      @param grid The grid where the ship is to be placed.
      @param row The starting row for the ship.
      @param col The starting column for the ship.
      @param size The size of the ship.
      @param horizontal The direction of the ship (horizontal or vertical).
      @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        int i2 = -1;
        int j2 = -1;
        int sizeI;
        int sizeJ;
        if(horizontal){
            if(col + size > 10){
                return false;
            }
            sizeI = size + 2;
            sizeJ = 2;
            if(col == 0){
                i2 = 0;
            }
            else if(col + size == 10) {
                sizeI = size;
            }
            if(row == 0){
                j2 = 0;
            }
            else if(row == 10){
                sizeJ = 1;
            }
        }
        else {
            if(row + size > 10){
                return false;
            }
            sizeI = 2;
            sizeJ = size + 2;
            if(row == 0){
                j2 = 0;
            }
            else if(row + size == 10) {
                sizeJ = size;
            }
            if(col == 0){
                i2 = 0;
            }
            else if(col == 10){
                sizeI = 1;
            }    
        }
        for(int i = i2; i < sizeI; i++){
            for (int j = j2; j < sizeJ; j++) {
                if(grid[row + j][col + i] != '~'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
      Manages a player's turn, allowing them to attack the opponent's grid
      and updates their tracking grid with hits or misses.

      @param opponentGrid The opponent's grid to attack.
      @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        System.out.print("Enter a target location (for example A7): ");
        String input = scanner.nextLine();
        if(!isValidInput(input)) {
            System.out.println("Invalid input. Don't do this again.");
            return;
        }
        else{
            int row = input.charAt(0) - 'A';
            int col = input.charAt(1) - '1';
            if(opponentGrid[row][col] == 'S'){
                trackingGrid[row][col] = 'X';
                if(shipSunk(opponentGrid, trackingGrid, row, col)){
                    System.out.println("You sunk a ship!");
                }
                else{
                    System.out.println("Hit!");
                }
            }
            else{
                trackingGrid[row][col] = 'O';
                System.out.println("Miss!");
            }
        }
    }

    /**
      Checks if the game is over by verifying if all ships are sunk.

      @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        //todo
        return false;
    }

    /**
      Checks if all ships have been destroyed on a given grid.

      @param grid The grid to check for destroyed ships.
      @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        //todo
        return true;
    }

    static boolean shipSunk(char[][] opponentGrid, char[][] trackingGrid, int row, int col) {
        int holdRow = row;
        boolean horizontal = false;
        int shipSize = 0;
        while(row >= 0 && opponentGrid[row][col] == 'S'){
            shipSize++;
            horizontal = true;
            if(trackingGrid[row][col] != 'X'){
                return false;
            }
            row--;
        }
        row = holdRow + 1;
        while(row < 10 && opponentGrid[row][col] == 'S'){
            shipSize++;
            horizontal = true;
            if(trackingGrid[row][col] != 'X'){
                return false;
            }
            row++;
        }
        row = holdRow;

        if(horizontal){
            int holdCol = col;
            while(col >= 0 && opponentGrid[row][col] == 'S'){
                shipSize++;
                if(trackingGrid[row][col] != 'X'){
                    return false;
                }
                col--;
            }
            col = holdCol + 1;
            while(col < 10 && opponentGrid[row][col] == 'S'){
                shipSize++;
                if(trackingGrid[row][col] != 'X'){
                    return false;
                }
                col++;
            }
            col = holdCol;
        }

        if(horizontal){
            for (int i = -1; i < shipSize; i++) {
                for (int j = -1; j < 2; j++) {
                    if(row + j >= 0 && row + j < 10 && col + i >= 0 && col + i < 10){
                        if(trackingGrid[row + j][col + i] == '~'){
                            trackingGrid[row + j][col + i] = 'O';
                        }
                    }
                }
            }
        }
        else{
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < shipSize; j++) {
                    if(row + j >= 0 && row + j < 10 && col + i >= 0 && col + i < 10){
                        if(trackingGrid[row + j][col + i] == '~'){
                            trackingGrid[row + j][col + i] = 'O';
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
      Validates if the user input is in the correct format (e.g., A5).

      @param input The input string to validate.
      @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        if(input.length() != 2){
            return false;
        }
        else if(input.charAt(0) < 'A' || input.charAt(0) > 'J'){
            return false;
        }
        else if(input.charAt(1) < '1' || input.charAt(1) > '0'){
            return false;
        }
        return true;
    }

    /**
      Prints the current state of the player's tracking grid.
      This method displays the grid, showing hits, misses, and untried locations.

      @param grid The tracking grid to print.
     */
    static void printGrid(char[][] grid) {
        System.out.println("  A B C D E F G H I J");
        for (int i = 1; i <= 10; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
