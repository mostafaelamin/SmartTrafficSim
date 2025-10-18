package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASS: TrafficFlowGame
 * AUTHORS: Zishan Vahora and Mostafa Elamin
 * DATE: October 2025
 * DESCRIPTION: A playable traffic simulation where the player must
 *              obey traffic lights and avoid collisions or going off-road.
 */
class TrafficFlowGame extends Game implements KeyListener {

    // ---------------- CONSTANTS (roads & intersection boxes) ----------------
    private static final int H_ROAD_Y = 250;  // horizontal road top
    private static final int H_ROAD_H = 100; // horizontal road height
    private static final int V_ROAD_X = 350;// vertical road left
    private static final int V_ROAD_W = 100;// vertical road width

    // Expanded intersection box for stricter red-light detection
    private static final Rectangle INTERSECTION_STRICT =
            new Rectangle(320, 220, 160, 160); // bigger than the road overlap

    // Stop zone for NPC vehicles (they pause here on red)
    private static final Rectangle INTERSECTION_STOP_ZONE =
            new Rectangle(340, 240, 120, 120);

    // ---------------- GAME STATE ----------------
    static int counter = 0;
    private Car playerCar;
    private List<TrafficElement> vehicles;
    private TrafficLight intersectionLight;
    private boolean gameOver = false;
    private String loseReason = "";

    // Lambda (counts toward “anonymous class / lambda” req)
    private final Runnable startMessage = () ->
            System.out.println("Traffic light initialized with automatic cycling logic.");

    public TrafficFlowGame() {
        super("Traffic Flow Game!", 800, 600);
        setupGame();

        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();

        startMessage.run();
    }

    // ---------------- GAME SETUP / RESET ----------------
    private void setupGame() {
        vehicles = new ArrayList<>();

        // Player car (controlled)
        Point[] carShape = {new Point(-5, 5), new Point(15, 5), new Point(15, -5), new Point(-5, -5)};
        playerCar = new Car(carShape, new Point(100, H_ROAD_Y + H_ROAD_H / 2 + 10), 90); // northbound lane

        // NPC vehicles (simple mix of directions)
        Point[] truckShape = {new Point(-10, 8), new Point(20, 8), new Point(20, -8), new Point(-10, -8)};
        Point[] ambShape = {new Point(-8, 5), new Point(18, 5), new Point(18, -5), new Point(-8, -5)};

        vehicles.add(new Truck(truckShape, new Point(-40, H_ROAD_Y + 40), 0)); // eastbound
        vehicles.add(new Ambulance(ambShape, new Point(width + 40, H_ROAD_Y + 60), 180));// westbound
        vehicles.add(new Truck(truckShape, new Point(V_ROAD_X + 60, -40), 90)); // southbound
        vehicles.add(new Car(carShape, new Point(V_ROAD_X + 40, height + 40), 270));// northbound
        vehicles.add(playerCar); // draw player last

        // Start light green
        intersectionLight = new TrafficLight(new Point(400, 300), LightState.GREEN);

        // Slightly faster overall feel
        for (TrafficElement v : vehicles) v.stepSize *= 1.5;
        playerCar.stepSize *= 1.3;
    }

    // ---------------- INNER CLASS: TRAFFIC LIGHT ----------------
    protected class TrafficLight {
        public Point position;
        public LightState state;
        private int lightTimer = 0;

        public TrafficLight(Point position, LightState initial) {
            this.position = position;
            this.state = initial;
        }

        public void update() {
            lightTimer++;
            // Faster cycling (~2 seconds per color @ ~10fps engine; adjust if your Game.update delay differs)
            if (lightTimer % 120 == 0) {
                if (state == LightState.GREEN) state = LightState.YELLOW;
                else if (state == LightState.YELLOW) state = LightState.RED;
                else state = LightState.GREEN;
            }
        }

        public void paint(Graphics brush) {
            brush.setColor(Color.DARK_GRAY);
            brush.fillRect((int) position.x - 15, (int) position.y - 45, 30, 90);

            brush.setColor(state == LightState.RED ? Color.RED : Color.GRAY);
            brush.fillOval((int) position.x - 5, (int) position.y - 25, 10, 10);

            brush.setColor(state == LightState.YELLOW ? Color.YELLOW : Color.GRAY);
            brush.fillOval((int) position.x - 5, (int) position.y - 5, 10, 10);

            brush.setColor(state == LightState.GREEN ? Color.GREEN : Color.GRAY);
            brush.fillOval((int) position.x - 5, (int) position.y + 15, 10, 10);
        }
    }

    private enum LightState { GREEN, YELLOW, RED }

    // ---------------- MAIN LOOP ----------------
    @Override
    public void paint(Graphics brush) {
        // 1) Background + “city” styling in corners
        drawScene(brush);

        if (gameOver) {
            drawGameOver(brush);
            return;
        }

        // 2) Update & draw traffic light
        intersectionLight.update();
        intersectionLight.paint(brush);

        // 3) Move/draw vehicles
        for (TrafficElement v : vehicles) {
            // Only stop NPC cars on red; player can still enter (and get flagged)
            if (v != playerCar && intersectionLight.state == LightState.RED && intersects(v, INTERSECTION_STOP_ZONE)) {
                v.paint(brush); // draw where it is (stopped)
                continue;
            }
            v.move();
            wrapAround(v);
            v.paint(brush);
        }

        // 4) Loss conditions
        // 4a) Collision: any two vehicles collide
        for (int i = 0; i < vehicles.size(); i++) {
            for (int j = i + 1; j < vehicles.size(); j++) {
                if (vehicles.get(i).collides(vehicles.get(j))) {
                    triggerLoss("Collision!");
                }
            }
        }

        // 4b) Red-light run: player’s center enters EXPANDED intersection area while red
        if (intersectionLight.state == LightState.RED && intersects(playerCar, INTERSECTION_STRICT)) {
            triggerLoss("Red Light Violation!");
        }

        // 4c) Off-road: player’s center is not within either road
        if (!isOnRoad(playerCar)) {
            triggerLoss("Off road!");
        }

        // 5) HUD
        brush.setColor(Color.WHITE);
        brush.setFont(new Font("Arial", Font.PLAIN, 12));
        brush.drawString("Obey the lights. Avoid crashes! Don’t leave the road.", 10, 15);
        brush.drawString("W/S: Move | A/D: Turn | Press R to Restart after losing", 10, 30);
        counter++;
    }

    // ---------------- DRAWING ----------------
    private void drawScene(Graphics brush) {
        // black base
        brush.setColor(Color.BLACK);
        brush.fillRect(0, 0, width, height);

        // Roads (full-width/height)
        brush.setColor(new Color(120, 120, 120)); // road gray
        brush.fillRect(0, H_ROAD_Y, width, H_ROAD_H);// horizontal
        brush.fillRect(V_ROAD_X, 0, V_ROAD_W, height); // vertical

        // Lane markings (simple dashed lines)
        brush.setColor(new Color(200, 200, 200));
        for (int x = 0; x < width; x += 30) {
            brush.fillRect(x, H_ROAD_Y + H_ROAD_H / 2 - 2, 15, 4);
        }
        for (int y = 0; y < height; y += 30) {
            brush.fillRect(V_ROAD_X + V_ROAD_W / 2 - 2, y, 4, 15);
        }

        // Sidewalks around roads
        brush.setColor(new Color(80, 80, 80));
        brush.fillRect(0, H_ROAD_Y - 12, width, 12);
        brush.fillRect(0, H_ROAD_Y + H_ROAD_H, width, 12);
        brush.fillRect(V_ROAD_X - 12, 0, 12, height);
        brush.fillRect(V_ROAD_X + V_ROAD_W, 0, 12, height);

        // Simple “city blocks” in four corners (buildings + windows + trees)
        drawBlock(brush, 0, 0, V_ROAD_X - 12, H_ROAD_Y - 12);  // top-left
        drawBlock(brush, V_ROAD_X + V_ROAD_W + 12, 0, width - (V_ROAD_X + V_ROAD_W + 12), H_ROAD_Y - 12); // top-right
        drawBlock(brush, 0, H_ROAD_Y + H_ROAD_H + 12, V_ROAD_X - 12, height - (H_ROAD_Y + H_ROAD_H + 12)); // bottom-left
        drawBlock(brush, V_ROAD_X + V_ROAD_W + 12, H_ROAD_Y + H_ROAD_H + 12,
        width - (V_ROAD_X + V_ROAD_W + 12), height - (H_ROAD_Y + H_ROAD_H + 12)); // bottom-right
    }

    private void drawBlock(Graphics g, int x, int y, int w, int h) {
        // building base
        g.setColor(new Color(35, 35, 45));
        g.fillRect(x, y, w, h);

        // windows grid
        g.setColor(new Color(220, 200, 120));
        int cols = Math.max(3, w / 60);
        int rows = Math.max(3, h / 60);
        int padX = 12, padY = 12, winW = 14, winH = 18;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int wx = x + padX + c * (winW + 18);
                int wy = y + padY + r * (winH + 18);
                if (wx + winW < x + w - padX && wy + winH < y + h - padY) {
                    g.fillRect(wx, wy, winW, winH);
                }
            }
        }

        // little park patches (trees)
        g.setColor(new Color(30, 110, 50));
        g.fillRoundRect(x + w - 60, y + h - 60, 50, 50, 10, 10);
        g.setColor(new Color(20, 85, 35));
        g.fillOval(x + w - 45, y + h - 45, 15, 15);
        g.fillOval(x + w - 30, y + h - 35, 12, 12);
        g.fillOval(x + w - 50, y + h - 30, 12, 12);
    }

    private void drawGameOver(Graphics brush) {
        brush.setColor(new Color(0, 0, 0, 150));
        brush.fillRect(0, 0, width, height);

        brush.setColor(Color.RED);
        brush.setFont(new Font("Arial", Font.BOLD, 42));
        brush.drawString("YOU LOST!", 270, 280);

        brush.setColor(Color.WHITE);
        brush.setFont(new Font("Arial", Font.PLAIN, 22));
        brush.drawString(loseReason, 320, 315);
        brush.drawString("Press R to Restart", 300, 350);
    }

    // ---------------- LOSS / CHECK HELPERS ----------------
    private void triggerLoss(String reason) {
        if (!gameOver) {
            gameOver = true;
            loseReason = reason;
            // stop player controls
            playerCar.forward = playerCar.backward = playerCar.left = playerCar.right = false;
            System.out.println("YOU LOST: " + reason);
        }
    }

    private void resetGame() {
        gameOver = false;
        loseReason = "";
        counter = 0;
        setupGame();
    }

    // Player is on-road if inside horizontal road **or** vertical road
    private boolean isOnRoad(TrafficElement v) {
        int x = (int) v.position.x;
        int y = (int) v.position.y;
        boolean onHorizontal = (y >= H_ROAD_Y) && (y <= H_ROAD_Y + H_ROAD_H);
        boolean onVertical = (x >= V_ROAD_X) && (x <= V_ROAD_X + V_ROAD_W);
        return onHorizontal || onVertical;
    }

    private boolean intersects(TrafficElement v, Rectangle r) {
        int x = (int) v.position.x;
        int y = (int) v.position.y;
        return r.contains(x, y);
    }

    private void wrapAround(TrafficElement v) {
        if (v.position.x > width + 40) v.position.x = -40;
        if (v.position.x < -40) v.position.x = width + 40;
        if (v.position.y > height + 40) v.position.y = -40;
        if (v.position.y < -40) v.position.y = height + 40;
    }

    // ---------------- INPUT ----------------
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameOver && key == KeyEvent.VK_R) {
            resetGame();
            return;
        }
        if (gameOver) return;

        if (key == KeyEvent.VK_W) playerCar.forward = true;
        else if (key == KeyEvent.VK_S) playerCar.backward = true;
        else if (key == KeyEvent.VK_A) playerCar.left = true;
        else if (key == KeyEvent.VK_D) playerCar.right = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) playerCar.forward = false;
        else if (key == KeyEvent.VK_S) playerCar.backward = false;
        else if (key == KeyEvent.VK_A) playerCar.left = false;
        else if (key == KeyEvent.VK_D) playerCar.right = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        TrafficFlowGame g = new TrafficFlowGame();
        g.on = true;  // continuous animation (handled by Game.update)
        g.repaint();
    }
}