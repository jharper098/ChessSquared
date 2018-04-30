import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

public class ChessBoard extends Screen
{
    public int player;
    private int turn;
    private ChessBlock selectedBlock = null;
    private final Color white = Color.WHITE;
    private final Color black = Color.BLACK;
    private final Color buttonColor1 = new Color(0, 130, 0);
    private final Color buttonColor2 = new Color(255, 255, 255);
    private ArrayList<ChessBlock> possibleMoves = new ArrayList<>();
    private ArrayList<ChessBlock> possibleCaptures = new ArrayList<>();
    private ArrayList<ChessBlock> dangerousBlocksForWhite = new ArrayList<>();
    private ArrayList<ChessBlock> dangerousBlocksForBlack = new ArrayList<>();
    private boolean gameOver = false;
    
    public PlayerWhite pWhite;
    public PlayerBlack pBlack;
    
    private final JFrame frame = createFrame("Chess");
    private final JPanel gui = new JPanel(new BorderLayout(3,3));
    private final JButton[][] chessBoardSquares = new JButton[8][8];
    private JPanel theChessBoard;
    private final JLabel message = new JLabel("Chess Champ is ready to play!");
    private static final String wCOLS = "ABCDEFGH";
    private static final String bCOLS = "HGFEDCBA";
    private static final String ROWS = "12345678";
    
    public static ChessBlock[][] board = new ChessBlock[8][8];
    
    private final Piece[] whitePawns = new Pawn[8];
    private final Piece[] whiteRooks = new Rook[2];
    private final Piece[] whiteKnights = new Knight[2];
    private final Piece[] whiteBishops = new Bishop[2];
    private Piece whiteQueen;
    private Piece whiteKing;
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    
    
    private final Piece[] blackPawns = new Pawn[8];
    private final Piece[] blackRooks = new Rook[2];
    private final Piece[] blackKnights = new Knight[2];
    private final Piece[] blackBishops = new Bishop[2];
    private Piece blackQueen;
    private Piece blackKing;
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    
    public ChessBoard(int player) 
    {
        this.player = player;
        turn = player;
        createGUI(player);
        frame.add(gui);
        frame.setVisible(true);
    }
    
    public final void createGUI(int player)
    {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        JButton newButton;
        JButton saveButton;
        JButton restoreButton;
        JButton resignButton;
        tools.add(newButton = new JButton("New")); // TODO - add functionality!
        newButton.addActionListener(new toolsListener());
        newButton.setActionCommand("New");
        tools.add(saveButton = new JButton("Save")); // TODO - add functionality!
        saveButton.addActionListener(new toolsListener());
        saveButton.setActionCommand("Save");
        tools.add(restoreButton = new JButton("Restore")); // TODO - add functionality!
        restoreButton.addActionListener(new toolsListener());
        restoreButton.setActionCommand("Restore");
        tools.addSeparator();
        tools.add(resignButton = new JButton("Resign")); // TODO - add functionality!
        resignButton.addActionListener(new toolsListener());
        resignButton.setActionCommand("Resign");
        tools.addSeparator();
        tools.add(message);
        
        theChessBoard = new JPanel(new GridLayout(0, 9));
        theChessBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(theChessBoard);
        
        setUpBoard(player);
        setUpPieces(player);
    }
    
    public void setUpBoard(int player)
    {    
        if(player == 1)
        {
            Insets buttonMargin = new Insets(0,0,0,0);
            for (int row = 7, actRow = 0; row >= 0; row--, actRow++)
            {
                for (int col = 0; col < chessBoardSquares[row].length; col++)
                {
                    JButton button = new JButton();
                    button.setMargin(buttonMargin);             
                    button.addActionListener(new MovePieceListener());
                    button.setActionCommand("" + Integer.toString(actRow) + 
                                                    Integer.toString(col));
                    // our chess pieces are 64x64 px in size, so we'll
                    // 'fill this in' using a transparent icon..
                    ImageIcon icon = new ImageIcon(
                            new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                    button.setIcon(icon);
                    if ((col % 2 == 0 && row % 2 == 0) || (col % 2 == 1 && row % 2 == 1))
                    {
                        button.setBackground(Color.WHITE);
                    }
                    else
                    {
                        button.setBackground(Color.GREEN);
                    }
                    chessBoardSquares[col][row] = button;
                }
            }

            for (int row = 0, actRow = 8; row < 8; row++, actRow--)
            {
                for (int col = 0; col < 8; col++)
                {
                    switch (col)
                    {
                        case 0:
                            theChessBoard.add(new JLabel("" + (actRow),
                                    SwingConstants.CENTER));
                        default:
                            theChessBoard.add(chessBoardSquares[col][row]);
                    }
                }
            }

            theChessBoard.add(new JLabel(""));

            for (int letter = 0; letter < 8; letter++)
            {
                theChessBoard.add(
                    new JLabel(wCOLS.substring(letter, letter + 1),
                    SwingConstants.CENTER));
            }
            
        }
        else if(player == 2)
        {    
            Insets buttonMargin = new Insets(0,0,0,0);
            for (int row = 7, actRow = 0; row >= 0; row--, actRow++)
            {
                for (int col = 0; col < chessBoardSquares[row].length; col++)
                {
                    JButton button = new JButton();
                    button.setMargin(buttonMargin);             
                    button.addActionListener(new MovePieceListener());
                    button.setActionCommand("" + Integer.toString(actRow) + 
                                                    Integer.toString(col));
                    // our chess pieces are 64x64 px in size, so we'll
                    // 'fill this in' using a transparent icon..
                    ImageIcon icon = new ImageIcon(
                            new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                    button.setIcon(icon);
                    if ((col % 2 == 0 && row % 2 == 0) || (col % 2 == 1 && row % 2 == 1))
                    {
                        button.setBackground(Color.WHITE);
                    }
                    else
                    {
                        button.setBackground(Color.GREEN);
                    }
                    chessBoardSquares[col][row] = button;
                }
            }

            for (int row = 0; row < 8; row++)
            {
                for (int col = 0; col < 8; col++)
                {
                    switch (col)
                    {
                        case 0:
                            theChessBoard.add(new JLabel("" + (row+1),
                                    SwingConstants.CENTER));
                        default:
                            theChessBoard.add(chessBoardSquares[col][row]);
                    }
                }
            }

            theChessBoard.add(new JLabel(""));

            for (int letter = 0; letter < 8; letter++)
            {
                theChessBoard.add(
                    new JLabel(bCOLS.substring(letter, letter+1),
                    SwingConstants.CENTER));
            }
        }
    }
    
    public void setUpPieces(int player)
    {    
        createBlocks(player);
        createPieces();
        putPiecesOnBlocks();
    }
    
    public void createBlocks(int player)
    {
        if(player == 1){
            board[0][0] = new ChessBlock("A1", chessBoardSquares[0][7], 0, 0, buttonColor1);
            board[1][0] = new ChessBlock("B1", chessBoardSquares[1][7], 1, 0, buttonColor2);
            board[2][0] = new ChessBlock("C1", chessBoardSquares[2][7], 2, 0, buttonColor1);
            board[3][0] = new ChessBlock("D1", chessBoardSquares[3][7], 3, 0, buttonColor2);
            board[4][0] = new ChessBlock("E1", chessBoardSquares[4][7], 4, 0, buttonColor1);
            board[5][0] = new ChessBlock("F1", chessBoardSquares[5][7], 5, 0, buttonColor2);
            board[6][0] = new ChessBlock("G1", chessBoardSquares[6][7], 6, 0, buttonColor1);
            board[7][0] = new ChessBlock("H1", chessBoardSquares[7][7], 7, 0, buttonColor2);
            
            board[0][1] = new ChessBlock("A2", chessBoardSquares[0][6], 0, 1, buttonColor2);
            board[1][1] = new ChessBlock("B2", chessBoardSquares[1][6], 1, 1, buttonColor1);
            board[2][1] = new ChessBlock("C2", chessBoardSquares[2][6], 2, 1, buttonColor2);
            board[3][1] = new ChessBlock("D2", chessBoardSquares[3][6], 3, 1, buttonColor1);
            board[4][1] = new ChessBlock("E2", chessBoardSquares[4][6], 4, 1, buttonColor2);
            board[5][1] = new ChessBlock("F2", chessBoardSquares[5][6], 5, 1, buttonColor1);
            board[6][1] = new ChessBlock("G2", chessBoardSquares[6][6], 6, 1, buttonColor2);
            board[7][1] = new ChessBlock("H2", chessBoardSquares[7][6], 7, 1, buttonColor1);
            
            board[0][2] = new ChessBlock("A3", chessBoardSquares[0][5], 0, 2, buttonColor1);
            board[1][2] = new ChessBlock("B3", chessBoardSquares[1][5], 1, 2, buttonColor2);
            board[2][2] = new ChessBlock("C3", chessBoardSquares[2][5], 2, 2, buttonColor1);
            board[3][2] = new ChessBlock("D3", chessBoardSquares[3][5], 3, 2, buttonColor2);
            board[4][2] = new ChessBlock("E3", chessBoardSquares[4][5], 4, 2, buttonColor1);
            board[5][2] = new ChessBlock("F3", chessBoardSquares[5][5], 5, 2, buttonColor2);
            board[6][2] = new ChessBlock("G3", chessBoardSquares[6][5], 6, 2, buttonColor1);
            board[7][2] = new ChessBlock("H3", chessBoardSquares[7][5], 7, 2, buttonColor2);
            
            board[0][3] = new ChessBlock("A4", chessBoardSquares[0][4], 0, 3, buttonColor2);
            board[1][3] = new ChessBlock("B4", chessBoardSquares[1][4], 1, 3, buttonColor1);
            board[2][3] = new ChessBlock("C4", chessBoardSquares[2][4], 2, 3, buttonColor2);
            board[3][3] = new ChessBlock("D4", chessBoardSquares[3][4], 3, 3, buttonColor1);
            board[4][3] = new ChessBlock("E4", chessBoardSquares[4][4], 4, 3, buttonColor2);
            board[5][3] = new ChessBlock("F4", chessBoardSquares[5][4], 5, 3, buttonColor1);
            board[6][3] = new ChessBlock("G4", chessBoardSquares[6][4], 6, 3, buttonColor2);
            board[7][3] = new ChessBlock("H4", chessBoardSquares[7][4], 7, 3, buttonColor1);
            
            board[0][4] = new ChessBlock("A5", chessBoardSquares[0][3], 0, 4, buttonColor1);
            board[1][4] = new ChessBlock("B5", chessBoardSquares[1][3], 1, 4, buttonColor2);
            board[2][4] = new ChessBlock("C5", chessBoardSquares[2][3], 2, 4, buttonColor1);
            board[3][4] = new ChessBlock("D5", chessBoardSquares[3][3], 3, 4, buttonColor2);
            board[4][4] = new ChessBlock("E5", chessBoardSquares[4][3], 4, 4, buttonColor1);
            board[5][4] = new ChessBlock("F5", chessBoardSquares[5][3], 5, 4, buttonColor2);
            board[6][4] = new ChessBlock("G5", chessBoardSquares[6][3], 6, 4, buttonColor1);
            board[7][4] = new ChessBlock("H5", chessBoardSquares[7][3], 7, 4, buttonColor2);
            
            board[0][5] = new ChessBlock("A6", chessBoardSquares[0][2], 0, 5, buttonColor2);
            board[1][5] = new ChessBlock("B6", chessBoardSquares[1][2], 1, 5, buttonColor1);
            board[2][5] = new ChessBlock("C6", chessBoardSquares[2][2], 2, 5, buttonColor2);
            board[3][5] = new ChessBlock("D6", chessBoardSquares[3][2], 3, 5, buttonColor1);
            board[4][5] = new ChessBlock("E6", chessBoardSquares[4][2], 4, 5, buttonColor2);
            board[5][5] = new ChessBlock("F6", chessBoardSquares[5][2], 5, 5, buttonColor1);
            board[6][5] = new ChessBlock("G6", chessBoardSquares[6][2], 6, 5, buttonColor2);
            board[7][5] = new ChessBlock("H6", chessBoardSquares[7][2], 7, 5, buttonColor1);
            
            board[0][6] = new ChessBlock("A7", chessBoardSquares[0][1], 0, 6, buttonColor1);
            board[1][6] = new ChessBlock("B7", chessBoardSquares[1][1], 1, 6, buttonColor2);
            board[2][6] = new ChessBlock("C7", chessBoardSquares[2][1], 2, 6, buttonColor1);
            board[3][6] = new ChessBlock("D7", chessBoardSquares[3][1], 3, 6, buttonColor2);
            board[4][6] = new ChessBlock("E7", chessBoardSquares[4][1], 4, 6, buttonColor1);
            board[5][6] = new ChessBlock("F7", chessBoardSquares[5][1], 5, 6, buttonColor2);
            board[6][6] = new ChessBlock("G7", chessBoardSquares[6][1], 6, 6, buttonColor1);
            board[7][6] = new ChessBlock("H7", chessBoardSquares[7][1], 7, 6, buttonColor2);
                                                      
            board[0][7] = new ChessBlock("A8", chessBoardSquares[0][0], 0, 7, buttonColor2);
            board[1][7] = new ChessBlock("B8", chessBoardSquares[1][0], 1, 7, buttonColor1);
            board[2][7] = new ChessBlock("C8", chessBoardSquares[2][0], 2, 7, buttonColor2);
            board[3][7] = new ChessBlock("D8", chessBoardSquares[3][0], 3, 7, buttonColor1);
            board[4][7] = new ChessBlock("E8", chessBoardSquares[4][0], 4, 7, buttonColor2);
            board[5][7] = new ChessBlock("F8", chessBoardSquares[5][0], 5, 7, buttonColor1);
            board[6][7] = new ChessBlock("G8", chessBoardSquares[6][0], 6, 7, buttonColor2);
            board[7][7] = new ChessBlock("H8", chessBoardSquares[7][0], 7, 7, buttonColor1);
        }
        else if(player == 2)
        {    
            board[0][0] = new ChessBlock("A1", chessBoardSquares[7][0], 0, 0, buttonColor1);
            board[1][0] = new ChessBlock("B1", chessBoardSquares[6][0], 1, 0, buttonColor2);
            board[2][0] = new ChessBlock("C1", chessBoardSquares[5][0], 2, 0, buttonColor1);
            board[3][0] = new ChessBlock("D1", chessBoardSquares[4][0], 3, 0, buttonColor2);
            board[4][0] = new ChessBlock("E1", chessBoardSquares[3][0], 4, 0, buttonColor1);
            board[5][0] = new ChessBlock("F1", chessBoardSquares[2][0], 5, 0, buttonColor2);
            board[6][0] = new ChessBlock("G1", chessBoardSquares[1][0], 6, 0, buttonColor1);
            board[7][0] = new ChessBlock("H1", chessBoardSquares[0][0], 7, 0, buttonColor2);
            
            board[0][1] = new ChessBlock("A2", chessBoardSquares[7][1], 0, 1, buttonColor2);
            board[1][1] = new ChessBlock("B2", chessBoardSquares[6][1], 1, 1, buttonColor1);
            board[2][1] = new ChessBlock("C2", chessBoardSquares[5][1], 2, 1, buttonColor2);
            board[3][1] = new ChessBlock("D2", chessBoardSquares[4][1], 3, 1, buttonColor1);
            board[4][1] = new ChessBlock("E2", chessBoardSquares[3][1], 4, 1, buttonColor2);
            board[5][1] = new ChessBlock("F2", chessBoardSquares[2][1], 5, 1, buttonColor1);
            board[6][1] = new ChessBlock("G2", chessBoardSquares[1][1], 6, 1, buttonColor2);
            board[7][1] = new ChessBlock("H2", chessBoardSquares[0][1], 7, 1, buttonColor1);
            
            board[0][2] = new ChessBlock("A3", chessBoardSquares[7][2], 0, 2, buttonColor1);
            board[1][2] = new ChessBlock("B3", chessBoardSquares[6][2], 1, 2, buttonColor2);
            board[2][2] = new ChessBlock("C3", chessBoardSquares[5][2], 2, 2, buttonColor1);
            board[3][2] = new ChessBlock("D3", chessBoardSquares[4][2], 3, 2, buttonColor2);
            board[4][2] = new ChessBlock("E3", chessBoardSquares[3][2], 4, 2, buttonColor1);
            board[5][2] = new ChessBlock("F3", chessBoardSquares[2][2], 5, 2, buttonColor2);
            board[6][2] = new ChessBlock("G3", chessBoardSquares[1][2], 6, 2, buttonColor1);
            board[7][2] = new ChessBlock("H3", chessBoardSquares[0][2], 7, 2, buttonColor2);
            
            board[0][3] = new ChessBlock("A4", chessBoardSquares[7][3], 0, 3, buttonColor2);
            board[1][3] = new ChessBlock("B4", chessBoardSquares[6][3], 1, 3, buttonColor1);
            board[2][3] = new ChessBlock("C4", chessBoardSquares[5][3], 2, 3, buttonColor2);
            board[3][3] = new ChessBlock("D4", chessBoardSquares[4][3], 3, 3, buttonColor1);
            board[4][3] = new ChessBlock("E4", chessBoardSquares[3][3], 4, 3, buttonColor2);
            board[5][3] = new ChessBlock("F4", chessBoardSquares[2][3], 5, 3, buttonColor1);
            board[6][3] = new ChessBlock("G4", chessBoardSquares[1][3], 6, 3, buttonColor2);
            board[7][3] = new ChessBlock("H4", chessBoardSquares[0][3], 7, 3, buttonColor1);
            
            board[0][4] = new ChessBlock("A5", chessBoardSquares[7][4], 0, 4, buttonColor1);
            board[1][4] = new ChessBlock("B5", chessBoardSquares[6][4], 1, 4, buttonColor2);
            board[2][4] = new ChessBlock("C5", chessBoardSquares[5][4], 2, 4, buttonColor1);
            board[3][4] = new ChessBlock("D5", chessBoardSquares[4][4], 3, 4, buttonColor2);
            board[4][4] = new ChessBlock("E5", chessBoardSquares[3][4], 4, 4, buttonColor1);
            board[5][4] = new ChessBlock("F5", chessBoardSquares[2][4], 5, 4, buttonColor2);
            board[6][4] = new ChessBlock("G5", chessBoardSquares[1][4], 6, 4, buttonColor1);
            board[7][4] = new ChessBlock("H5", chessBoardSquares[0][4], 7, 4, buttonColor2);
            
            board[0][5] = new ChessBlock("A6", chessBoardSquares[7][5], 0, 5, buttonColor2);
            board[1][5] = new ChessBlock("B6", chessBoardSquares[6][5], 1, 5, buttonColor1);
            board[2][5] = new ChessBlock("C6", chessBoardSquares[5][5], 2, 5, buttonColor2);
            board[3][5] = new ChessBlock("D6", chessBoardSquares[4][5], 3, 5, buttonColor1);
            board[4][5] = new ChessBlock("E6", chessBoardSquares[3][5], 4, 5, buttonColor2);
            board[5][5] = new ChessBlock("F6", chessBoardSquares[2][5], 5, 5, buttonColor1);
            board[6][5] = new ChessBlock("G6", chessBoardSquares[1][5], 6, 5, buttonColor2);
            board[7][5] = new ChessBlock("H6", chessBoardSquares[0][5], 7, 5, buttonColor1);

            board[0][6] = new ChessBlock("A7", chessBoardSquares[7][6], 0, 6, buttonColor1);
            board[1][6] = new ChessBlock("B7", chessBoardSquares[6][6], 1, 6, buttonColor2);
            board[2][6] = new ChessBlock("C7", chessBoardSquares[5][6], 2, 6, buttonColor1);
            board[3][6] = new ChessBlock("D7", chessBoardSquares[4][6], 3, 6, buttonColor2);
            board[4][6] = new ChessBlock("E7", chessBoardSquares[3][6], 4, 6, buttonColor1);
            board[5][6] = new ChessBlock("F7", chessBoardSquares[2][6], 5, 6, buttonColor2);
            board[6][6] = new ChessBlock("G7", chessBoardSquares[1][6], 6, 6, buttonColor1);
            board[7][6] = new ChessBlock("H7", chessBoardSquares[0][6], 7, 6, buttonColor2);
            
            board[0][7] = new ChessBlock("A8", chessBoardSquares[7][7], 0, 7, buttonColor2);
            board[1][7] = new ChessBlock("B8", chessBoardSquares[6][7], 1, 7, buttonColor1);
            board[2][7] = new ChessBlock("C8", chessBoardSquares[5][7], 2, 7, buttonColor2);
            board[3][7] = new ChessBlock("D8", chessBoardSquares[4][7], 3, 7, buttonColor1);
            board[4][7] = new ChessBlock("E8", chessBoardSquares[3][7], 4, 7, buttonColor2);
            board[5][7] = new ChessBlock("F8", chessBoardSquares[2][7], 5, 7, buttonColor1);
            board[6][7] = new ChessBlock("G8", chessBoardSquares[1][7], 6, 7, buttonColor2);
            board[7][7] = new ChessBlock("H8", chessBoardSquares[0][7], 7, 7, buttonColor1);
        }
    }
    
    public void createPieces()
    {
        whitePawns[0] = new Pawn(white, board[0][1]);
        whitePawns[1] = new Pawn(white, board[1][1]);
        whitePawns[2] = new Pawn(white, board[2][1]);
        whitePawns[3] = new Pawn(white, board[3][1]);
        whitePawns[4] = new Pawn(white, board[4][1]);
        whitePawns[5] = new Pawn(white, board[5][1]);
        whitePawns[6] = new Pawn(white, board[6][1]);
        whitePawns[7] = new Pawn(white, board[7][1]);
        whiteRooks[0] = new Rook(white, board[0][0]); 
        whiteRooks[1] = new Rook(white, board[7][0]);
        whiteKnights[0] = new Knight(white, board[1][0]); 
        whiteKnights[1] = new Knight(white, board[6][0]);
        whiteBishops[0] = new Bishop(white, board[2][0]); 
        whiteBishops[1] = new Bishop(white, board[5][0]);
        whiteKing = new King(white, board[3][0]);
        whiteQueen = new Queen(white, board[4][0]);
        
        whitePieces.add(whitePawns[0]);
        whitePieces.add(whitePawns[1]);
        whitePieces.add(whitePawns[2]);
        whitePieces.add(whitePawns[3]);
        whitePieces.add(whitePawns[4]);
        whitePieces.add(whitePawns[5]);
        whitePieces.add(whitePawns[6]);
        whitePieces.add(whitePawns[7]);
        whitePieces.add(whiteRooks[0]);
        whitePieces.add(whiteRooks[1]);
        whitePieces.add(whiteKnights[0]);
        whitePieces.add(whiteKnights[1]);
        whitePieces.add(whiteBishops[0]);
        whitePieces.add(whiteBishops[1]);
        whitePieces.add(whiteQueen);
        whitePieces.add(whiteKing);
        
        blackPawns[0] = new Pawn(black, board[0][6]);
        blackPawns[1] = new Pawn(black, board[1][6]);
        blackPawns[2] = new Pawn(black, board[2][6]);
        blackPawns[3] = new Pawn(black, board[3][6]);
        blackPawns[4] = new Pawn(black, board[4][6]);
        blackPawns[5] = new Pawn(black, board[5][6]);
        blackPawns[6] = new Pawn(black, board[6][6]);
        blackPawns[7] = new Pawn(black, board[7][6]);
        blackRooks[0] = new Rook(black, board[0][7]); 
        blackRooks[1] = new Rook(black, board[7][7]);
        blackKnights[0] = new Knight(black, board[1][7]); 
        blackKnights[1] = new Knight(black, board[6][7]);
        blackBishops[0] = new Bishop(black, board[2][7]); 
        blackBishops[1] = new Bishop(black, board[5][7]);
        blackKing = new King(black, board[3][7]);
        blackQueen = new Queen(black, board[4][7]);
        
        blackPieces.add(blackPawns[0]);
        blackPieces.add(blackPawns[1]);
        blackPieces.add(blackPawns[2]);
        blackPieces.add(blackPawns[3]);
        blackPieces.add(blackPawns[4]);
        blackPieces.add(blackPawns[5]);
        blackPieces.add(blackPawns[6]);
        blackPieces.add(blackPawns[7]);
        blackPieces.add(blackRooks[0]);
        blackPieces.add(blackRooks[1]);
        blackPieces.add(blackKnights[0]);
        blackPieces.add(blackKnights[1]);
        blackPieces.add(blackBishops[0]);
        blackPieces.add(blackBishops[1]);
        blackPieces.add(blackQueen);
        blackPieces.add(blackKing);
    }
    
    public void putPiecesOnBlocks()
    {
        board[0][0].setPiece(whiteRooks[0]);
        board[1][0].setPiece(whiteKnights[0]);
        board[2][0].setPiece(whiteBishops[0]);
        board[3][0].setPiece(whiteKing);
        board[4][0].setPiece(whiteQueen);
        board[5][0].setPiece(whiteBishops[1]);
        board[6][0].setPiece(whiteKnights[1]);
        board[7][0].setPiece(whiteRooks[1]);
        
        board[0][1].setPiece(whitePawns[0]);
        board[1][1].setPiece(whitePawns[1]);
        board[2][1].setPiece(whitePawns[2]);
        board[3][1].setPiece(whitePawns[3]);
        board[4][1].setPiece(whitePawns[4]);
        board[5][1].setPiece(whitePawns[5]);
        board[6][1].setPiece(whitePawns[6]);
        board[7][1].setPiece(whitePawns[7]);
        
        board[0][6].setPiece(blackPawns[0]);
        board[1][6].setPiece(blackPawns[1]);
        board[2][6].setPiece(blackPawns[2]);
        board[3][6].setPiece(blackPawns[3]);
        board[4][6].setPiece(blackPawns[4]);
        board[5][6].setPiece(blackPawns[5]);
        board[6][6].setPiece(blackPawns[6]);
        board[7][6].setPiece(blackPawns[7]);
        
        board[0][7].setPiece(blackRooks[0]);
        board[1][7].setPiece(blackKnights[0]);
        board[2][7].setPiece(blackBishops[0]);
        board[3][7].setPiece(blackKing);
        board[4][7].setPiece(blackQueen);
        board[5][7].setPiece(blackBishops[1]);
        board[6][7].setPiece(blackKnights[1]);
        board[7][7].setPiece(blackRooks[1]);
    }
    
    public class toolsListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e){
            //new save restore resign
            if("new".equals(e.getActionCommand())){
                Game.newGame();
                frame.setVisible(false);
            }else if("save".equals(e.getActionCommand())){
                
            }else if("restore".equals(e.getActionCommand())){
                
            }else if("resign".equals(e.getActionCommand())){
                
            }
        }
    }
    
    private boolean isPieceSelected()
    {
        return selectedBlock != null;
    }
    
    private void blockClicked(ChessBlock chessBlock)
    {
        if (!gameOver)
        {
            if (chessBlock.hasPiece())
            {
                if (isPieceSelected() && chessBlock != selectedBlock && possibleCaptures.contains(chessBlock))
                {
                    capturePiece(chessBlock);
                }
                else if ((turn == 1 && chessBlock.getPiece().color == Color.WHITE) || (turn == 2 && chessBlock.getPiece().color == Color.BLACK))
                {
                    selectBlock(chessBlock);
                }
            }
            else
            {
                if (isPieceSelected())
                {
                    if (chessBlock != selectedBlock)
                    {
                        if (possibleMoves.contains(chessBlock))
                        {
                            moveToBlock(chessBlock);
                        }
                    }
                }
            }
        }
    }
    
    private void selectBlock(ChessBlock chessBlock)
    {
        if (selectedBlock != null)
        {
            selectedBlock.setSelectedPieceButtonColor(false);
        }
        selectedBlock = chessBlock;
        selectedBlock.setSelectedPieceButtonColor(true);
        getPossibledMovesAndCaptures();
    }
    
    private void moveToBlock(ChessBlock chessBlock)
    {
        clearPossibleMovesAndCaptures();
        selectedBlock.getPiece().move(chessBlock);
        selectedBlock.setSelectedPieceButtonColor(false);
        selectedBlock = null;
        switchTurn();
    }
    
    private void capturePiece(ChessBlock chessBlock)
    {
        chessBlock.setPiece(null);
        clearPossibleMovesAndCaptures();
        selectedBlock.getPiece().move(chessBlock);
        selectedBlock.setSelectedPieceButtonColor(false);
        selectedBlock = null;
        switchTurn();
    }
    
    private void switchTurn()
    {
        if (turn == 1)
        {
            turn = 2;
            findDangerousBlocksForBlack();
            System.out.println("found dangerous for black");
            if (dangerousBlocksForBlack.contains(blackKing.currentPosition))
            {
                System.out.println("black in check");
                selectBlock(blackKing.currentPosition);
                checkForWinner();
            }
        }
        else
        {
            turn = 1;
            findDangerousBlocksForWhite();
            System.out.println("found dangerous for white");
            if (dangerousBlocksForWhite.contains(whiteKing.currentPosition))
            {
                System.out.println("White in check");
                selectBlock(whiteKing.currentPosition);
                checkForWinner();
            }        
        }
    }
    
    private void checkForWinner()
    {
        if (possibleMoves.isEmpty() && possibleCaptures.isEmpty())
        {
            gameOver = true;
            for (int y = 0; y < 8; y++)
            {
                for (int x = 0; x < 8; x++)
                {
                    board[x][y].setWinnerButtonColor(gameOver, selectedBlock.getPiece().color);
                }
            }
        }
    }
    
    private void findDangerousBlocksForWhite()
    {
        dangerousBlocksForWhite.forEach(block -> block.setDangerousButtonColor(false));
        dangerousBlocksForWhite.clear();
        ArrayList<ChessBlock> dangerousBlocks = new ArrayList<>();
        for (int i = 0; i < blackPieces.size(); i++)
        {
            dangerousBlocks = findDangerousBlocks(blackPieces.get(i));
            dangerousBlocks.forEach(block -> dangerousBlocksForWhite.add(block));
            dangerousBlocks.clear();
        }
        // setting this to true or false determines if dangerous squares are highlighted
        dangerousBlocksForWhite.forEach(block -> block.setDangerousButtonColor(false));
    }
    
    private void findDangerousBlocksForBlack()
    {
        dangerousBlocksForWhite.forEach(block -> block.setDangerousButtonColor(false));
        dangerousBlocksForBlack.clear();
        ArrayList<ChessBlock> dangerousBlocks = new ArrayList<>();
        for (int i = 0; i < whitePieces.size(); i++)
        {
            dangerousBlocks = findDangerousBlocks(whitePieces.get(i));
            dangerousBlocks.forEach(block -> dangerousBlocksForBlack.add(block));
            dangerousBlocks.clear();
            // setting this to true or false determines if dangerous squares are highlighted
            dangerousBlocksForWhite.forEach(block -> block.setDangerousButtonColor(false));
        }
    }
    
    private ArrayList<ChessBlock> findDangerousBlocks(Piece piece)
    {
        ArrayList<ChessBlock> dangerousBlocks = new ArrayList<>();
        
        if ("pawn".equals(piece.pieceName))
        {
            if (piece.color == Color.WHITE)
            {
                if (piece.currentPosition.y + 1 < 8)
                {
                    if (piece.currentPosition.x - 1 >= 0)
                    {
                        if (!board[piece.currentPosition.x - 1][piece.currentPosition.y + 1].hasPiece())
                        {
                            dangerousBlocks.add(board[piece.currentPosition.x - 1][piece.currentPosition.y + 1]);
                        }
                    }
                    
                    if (piece.currentPosition.x + 1 < 8)
                    {
                        if (!board[piece.currentPosition.x + 1][piece.currentPosition.y + 1].hasPiece())
                        {
                            dangerousBlocks.add(board[piece.currentPosition.x + 1][piece.currentPosition.y + 1]);
                        }
                    } 
                }
            }
            else
            {
                if (piece.currentPosition.y - 1 >= 0)
                {
                    if (piece.currentPosition.x - 1 >= 0)
                    {
                        if (!board[piece.currentPosition.x - 1][piece.currentPosition.y - 1].hasPiece())
                        {
                            dangerousBlocks.add(board[piece.currentPosition.x - 1][piece.currentPosition.y - 1]);
                        }
                    }
                    
                    if (piece.currentPosition.x + 1 < 8)
                    {
                        if (!board[piece.currentPosition.x + 1][piece.currentPosition.y - 1].hasPiece())
                        {
                            dangerousBlocks.add(board[piece.currentPosition.x + 1][piece.currentPosition.y - 1]);
                        }
                    } 
                }
            }
            return dangerousBlocks;
        }
        else if ("rook".equals(piece.pieceName))
        {
            getPossibleRookMovesAndCaptures(piece.currentPosition);
            possibleMoves.forEach((block) -> { dangerousBlocks.add(block); });
            possibleMoves.clear();
            possibleCaptures.clear();
            return dangerousBlocks;
        }
        else if ("knight".equals(piece.pieceName))
        {
            getPossibleKnightMovesAndCaptures(piece.currentPosition);
            possibleMoves.forEach((block) -> { dangerousBlocks.add(block); });
            possibleMoves.clear();
            possibleCaptures.clear();
            return dangerousBlocks;
        }
        else if ("bishop".equals(piece.pieceName))
        {
            getPossibleBishopMovesAndCaptures(piece.currentPosition);
            possibleMoves.forEach((block) -> { dangerousBlocks.add(block); });
            possibleMoves.clear();
            possibleCaptures.clear();
            return dangerousBlocks;
        }
        else if ("queen".equals(piece.pieceName))
        {
            getPossibleQueenMovesAndCaptures(piece.currentPosition);
            possibleMoves.forEach((block) -> { dangerousBlocks.add(block); });
            possibleMoves.clear();
            possibleCaptures.clear();
            return dangerousBlocks;
        }
        else if ("king".equals(piece.pieceName))
        {
            getPossibleKingMovesAndCaptures(piece.currentPosition);
            possibleMoves.forEach((block) -> { dangerousBlocks.add(block); });
            possibleMoves.clear();
            possibleCaptures.clear();
            return dangerousBlocks;
        }
        else
        {
            return dangerousBlocks;
        }
    }
    
    private void clearPossibleMovesAndCaptures()
    {
        possibleMoves.forEach(block -> block.setPossibleMoveButtonColor(false));
        possibleMoves.clear();
        possibleCaptures.forEach(block -> block.setPossibleCaptureButtonColor(false));
        possibleCaptures.clear();
    }
    
    private void getPossibledMovesAndCaptures()
    {
        clearPossibleMovesAndCaptures();
        
        if ("pawn".equals(selectedBlock.getPiece().pieceName))
        {
            getPossiblePawnMovesAndCaptures(selectedBlock);
        }
        else if ("rook".equals(selectedBlock.getPiece().pieceName))
        {
            getPossibleRookMovesAndCaptures(selectedBlock);
        }
        else if ("knight".equals(selectedBlock.getPiece().pieceName))
        {
            getPossibleKnightMovesAndCaptures(selectedBlock);
        }
        else if ("bishop".equals(selectedBlock.getPiece().pieceName))
        {
            getPossibleBishopMovesAndCaptures(selectedBlock);
        }
        else if ("queen".equals(selectedBlock.getPiece().pieceName))
        {
            getPossibleQueenMovesAndCaptures(selectedBlock);
        }
        else if ("king".equals(selectedBlock.getPiece().pieceName))
        {
            getPossibleKingMovesAndCaptures(selectedBlock);
        }
      
        possibleMoves.forEach(block -> block.setPossibleMoveButtonColor(true));
        possibleCaptures.forEach(block -> block.setPossibleCaptureButtonColor(true));
    }
        
    private void getPossiblePawnMovesAndCaptures(ChessBlock block)
    {
        if (block.getPiece().color == Color.WHITE)
        {
            if (block.y + 1 < 8)
            {
                if (!board[block.x][block.y + 1].hasPiece())
                {
                    possibleMoves.add(board[block.x][block.y + 1]);
                    
                    if (!block.getPiece().hasMoved)
                    {
                        if (!board[block.x][block.y + 2].hasPiece())
                        {
                            possibleMoves.add(board[block.x][block.y + 2]);
                        }
                    }
                }
                
                if (block.x + 1 < 8)
                {
                    if (board[block.x + 1][block.y + 1].hasPiece())
                    {
                        if (board[block.x + 1][block.y + 1].getPiece().color != block.getPiece().color)
                        {
                            possibleCaptures.add(board[block.x + 1][block.y + 1]);
                        }
                    }
                }
                
                if (block.x - 1 >= 0)
                {
                    if (board[block.x - 1][block.y + 1].hasPiece())
                    {
                        if (board[block.x - 1][block.y + 1].getPiece().color != block.getPiece().color)
                        {
                            possibleCaptures.add(board[block.x - 1][block.y + 1]);
                        }
                    }
                }
            }
        }
        else
        {
            if (block.y - 1 > 0)
            {
                if (!board[block.x][block.y - 1].hasPiece())
                {
                    possibleMoves.add(board[block.x][block.y - 1]);
                    
                    if (!block.getPiece().hasMoved)
                    {
                        if (!board[block.x][block.y - 2].hasPiece())
                        {
                            possibleMoves.add(board[block.x][block.y - 2]);
                        }
                    }
                }
                
                if (block.x + 1 < 8)
                {
                    if (board[block.x + 1][block.y - 1].hasPiece())
                    {
                        if (board[block.x + 1][block.y - 1].getPiece().color != block.getPiece().color)
                        {
                            possibleCaptures.add(board[block.x + 1][block.y - 1]);
                        }
                    }
                }
                
                if (block.x - 1 >= 0)
                {
                    if (board[block.x - 1][block.y - 1].hasPiece())
                    {
                        if (board[block.x - 1][block.y - 1].getPiece().color != block.getPiece().color)
                        {
                            possibleCaptures.add(board[block.x - 1][block.y - 1]);
                        }
                    }
                }
            }
        }
    }
    
    private void getPossibleRookMovesAndCaptures(ChessBlock block)
    {  
        for (int y = block.y + 1; y < 8; y++)
        {
            if (board[block.x][y].hasPiece())
            {
                if (board[block.x][y].getPiece().color != block.getPiece().color)
                {
                    possibleCaptures.add(board[block.x][y]);
                }
                break;
            }
            else
            {
                possibleMoves.add(board[block.x][y]);
            }
        }
        
        for (int y = block.y - 1; y >= 0; y--)
        {
            if (board[block.x][y].hasPiece())
            {
                if (board[block.x][y].getPiece().color != block.getPiece().color)
                {
                    possibleCaptures.add(board[block.x][y]);
                }
                break;
            }
            else
            {
                possibleMoves.add(board[block.x][y]);
            }
        }
        
        for (int x = block.x + 1; x < 8; x++)
        {
            if (board[x][block.y].hasPiece())
            {
                if (board[x][block.y].getPiece().color != block.getPiece().color)
                {
                    possibleCaptures.add(board[x][block.y]);
                }
                break;
            }
            else
            {
                possibleMoves.add(board[x][block.y]);
            }
        }
        
        for (int x = block.x - 1; x >= 0; x--)
        {
            if (board[x][block.y].hasPiece())
            {
                if (board[x][block.y].getPiece().color != block.getPiece().color)
                {
                    possibleCaptures.add(board[x][block.y]);
                }
                break;
            }
            else
            {
                possibleMoves.add(board[x][block.y]);
            }
        }
    }
    
    private void getPossibleKnightMovesAndCaptures(ChessBlock block)
    {
        int[] x = { block.x - 2, block.x - 2, block.x + 2, block.x + 2,
                    block.x - 1, block.x + 1, block.x - 1, block.x + 1 };
        int[] y = { block.y - 1, block.y + 1, block.y - 1, block.y + 1,
                    block.y - 2, block.y - 2, block.y + 2, block.y + 2 };

        for (int i = 0; i < 8; i++)
        {
            if (x[i] >= 0 && x[i] < 8 && y[i] >= 0 && y[i] < 8)
            {
                if (!board[x[i]][y[i]].hasPiece())
                {
                    possibleMoves.add(board[x[i]][y[i]]);
                }
                else if (board[x[i]][y[i]].getPiece().color != block.getPiece().color)
                {
                    possibleCaptures.add(board[x[i]][y[i]]);
                }
            }
        }
    }
    
    private void getPossibleBishopMovesAndCaptures(ChessBlock block)
    {
        for (int i = 1; i < 8; i++)
        {
            if (block.x + i > 7 || block.y + i > 7)
            {
                break;
            }
            else
            {
                if (board[block.x + i][block.y + i].hasPiece())
                {
                    if (board[block.x + i][block.y + i].getPiece().color != block.getPiece().color)
                    {
                        possibleCaptures.add(board[block.x + i][block.y + i]);
                    }
                    break;
                }
                else
                {
                    possibleMoves.add(board[block.x + i][block.y + i]);
                }
            }
        }
        
        for (int i = 1; i < 8; i++)
        {
            if (block.x - i < 0 || block.y + i > 7)
            {
                break;
            }
            else
            {
                if (board[block.x - i][block.y + i].hasPiece())
                {
                    if (board[block.x - i][block.y + i].getPiece().color != block.getPiece().color)
                    {
                        possibleCaptures.add(board[block.x - i][block.y + i]);
                    }
                    break;
                }
                else
                {
                    possibleMoves.add(board[block.x - i][block.y + i]);
                }
            }
        }
        
        for (int i = 1; i < 8; i++)
        {
            if (block.x - i < 0 || block.y - i < 0)
            {
                break;
            }
            else
            {
                if (board[block.x - i][block.y - i].hasPiece())
                {
                    if (board[block.x - i][block.y - i].getPiece().color != block.getPiece().color)
                    {
                        possibleCaptures.add(board[block.x - i][block.y - i]);
                    }
                    break;
                }
                else
                {
                    possibleMoves.add(board[block.x - i][block.y - i]);
                }
            }
        }
        
        for (int i = 1; i < 8; i++)
        {
            if (block.x + i > 7 || block.y - i < 0)
            {
                break;
            }
            else
            {
                if (board[block.x + i][block.y - i].hasPiece())
                {
                    if (board[block.x + i][block.y - i].getPiece().color != block.getPiece().color)
                    {
                        possibleCaptures.add(board[block.x + i][block.y - i]);
                    }
                    break;
                }
                else
                {
                    possibleMoves.add(board[block.x + i][block.y - i]);
                }
            }
        }
    }
    
    private void getPossibleQueenMovesAndCaptures(ChessBlock block)
    {
        getPossibleRookMovesAndCaptures(block);
        getPossibleBishopMovesAndCaptures(block);
    }
    
    private void getPossibleKingMovesAndCaptures(ChessBlock block)
    {
        int[][] xy = { {block.x, block.y + 1},
                       {block.x + 1, block.y + 1},
                       {block.x + 1, block.y},
                       {block.x + 1, block.y - 1},
                       {block.x, block.y - 1},
                       {block.x - 1, block.y - 1},
                       {block.x - 1, block.y},
                       {block.x - 1, block.y + 1} };
        
        for (int i = 0; i < 8; i++)
        {
            if (xy[i][0] >= 0 && xy[i][0] < 8 && xy[i][1] >= 0 && xy[i][1] < 8)
            {
                if (!board[xy[i][0]][xy[i][1]].hasPiece())
                {
                    if (block.getPiece().color == Color.WHITE)
                    {
                        if (!dangerousBlocksForWhite.contains(board[xy[i][0]][xy[i][1]]))
                        {
                            possibleMoves.add(board[xy[i][0]][xy[i][1]]);
                        }
                    }
                    else
                    {
                        if (!dangerousBlocksForBlack.contains(board[xy[i][0]][xy[i][1]]))
                        {
                            possibleMoves.add(board[xy[i][0]][xy[i][1]]);
                        }
                    }
                }
                else if (board[xy[i][0]][xy[i][1]].getPiece().color != block.getPiece().color)
                {
                    if (block.getPiece().color == Color.WHITE)
                    {
                        if (!dangerousBlocksForWhite.contains(board[xy[i][0]][xy[i][1]]))
                        {
                            possibleCaptures.add(board[xy[i][0]][xy[i][1]]);
                        }
                    }
                    else
                    {
                        if (!dangerousBlocksForBlack.contains(board[xy[i][0]][xy[i][1]]))
                        {
                            possibleCaptures.add(board[xy[i][0]][xy[i][1]]);
                        }
                    }
                }
            }
        }
    }
    
    public class MovePieceListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (player == 1){
                if("00".equals(e.getActionCommand())){
                    blockClicked(board[0][0]);
                }else if("01".equals(e.getActionCommand())){
                    blockClicked(board[1][0]);
                }else if("02".equals(e.getActionCommand())){
                    blockClicked(board[2][0]);
                }else if("03".equals(e.getActionCommand())){
                    blockClicked(board[3][0]);
                }else if("04".equals(e.getActionCommand())){
                    blockClicked(board[4][0]);
                }else if("05".equals(e.getActionCommand())){
                    blockClicked(board[5][0]);
                }else if("06".equals(e.getActionCommand())){
                    blockClicked(board[6][0]);
                }else if("07".equals(e.getActionCommand())){
                    blockClicked(board[7][0]);
                }else if("10".equals(e.getActionCommand())){
                    blockClicked(board[0][1]);
                }else if("11".equals(e.getActionCommand())){
                    blockClicked(board[1][1]);
                }else if("12".equals(e.getActionCommand())){
                    blockClicked(board[2][1]);
                }else if("13".equals(e.getActionCommand())){
                    blockClicked(board[3][1]);
                }else if("14".equals(e.getActionCommand())){
                    blockClicked(board[4][1]);
                }else if("15".equals(e.getActionCommand())){
                    blockClicked(board[5][1]);
                }else if("16".equals(e.getActionCommand())){
                    blockClicked(board[6][1]);
                }else if("17".equals(e.getActionCommand())){
                    blockClicked(board[7][1]);
                }else if("20".equals(e.getActionCommand())){
                    blockClicked(board[0][2]);
                }else if("21".equals(e.getActionCommand())){
                    blockClicked(board[1][2]);
                }else if("22".equals(e.getActionCommand())){
                    blockClicked(board[2][2]);
                }else if("23".equals(e.getActionCommand())){
                    blockClicked(board[3][2]);
                }else if("24".equals(e.getActionCommand())){
                    blockClicked(board[4][2]);
                }else if("25".equals(e.getActionCommand())){
                    blockClicked(board[5][2]);
                }else if("26".equals(e.getActionCommand())){
                    blockClicked(board[6][2]);
                }else if("27".equals(e.getActionCommand())){
                    blockClicked(board[7][2]);
                }else if("30".equals(e.getActionCommand())){
                    blockClicked(board[0][3]);
                }else if("31".equals(e.getActionCommand())){
                    blockClicked(board[1][3]);
                }else if("32".equals(e.getActionCommand())){
                    blockClicked(board[2][3]);
                }else if("33".equals(e.getActionCommand())){
                    blockClicked(board[3][3]);
                }else if("34".equals(e.getActionCommand())){
                    blockClicked(board[4][3]);
                }else if("35".equals(e.getActionCommand())){
                    blockClicked(board[5][3]);
                }else if("36".equals(e.getActionCommand())){
                    blockClicked(board[6][3]);
                }else if("37".equals(e.getActionCommand())){
                    blockClicked(board[7][3]);
                }else if("40".equals(e.getActionCommand())){
                    blockClicked(board[0][4]);
                }else if("41".equals(e.getActionCommand())){
                    blockClicked(board[1][4]);
                }else if("42".equals(e.getActionCommand())){
                    blockClicked(board[2][4]);
                }else if("43".equals(e.getActionCommand())){
                    blockClicked(board[3][4]);
                }else if("44".equals(e.getActionCommand())){
                    blockClicked(board[4][4]);
                }else if("45".equals(e.getActionCommand())){
                    blockClicked(board[5][4]);
                }else if("46".equals(e.getActionCommand())){
                    blockClicked(board[6][4]);
                }else if("47".equals(e.getActionCommand())){
                    blockClicked(board[7][4]);
                }else if("50".equals(e.getActionCommand())){
                    blockClicked(board[0][5]);
                }else if("51".equals(e.getActionCommand())){
                    blockClicked(board[1][5]);
                }else if("52".equals(e.getActionCommand())){
                    blockClicked(board[2][5]);
                }else if("53".equals(e.getActionCommand())){
                    blockClicked(board[3][5]);
                }else if("54".equals(e.getActionCommand())){
                    blockClicked(board[4][5]);
                }else if("55".equals(e.getActionCommand())){
                    blockClicked(board[5][5]);
                }else if("56".equals(e.getActionCommand())){
                    blockClicked(board[6][5]);
                }else if("57".equals(e.getActionCommand())){
                    blockClicked(board[7][5]);
                }else if("60".equals(e.getActionCommand())){
                    blockClicked(board[0][6]);
                }else if("61".equals(e.getActionCommand())){
                    blockClicked(board[1][6]);
                }else if("62".equals(e.getActionCommand())){
                    blockClicked(board[2][6]);
                }else if("63".equals(e.getActionCommand())){
                    blockClicked(board[3][6]);
                }else if("64".equals(e.getActionCommand())){
                    blockClicked(board[4][6]);
                }else if("65".equals(e.getActionCommand())){
                    blockClicked(board[5][6]);
                }else if("66".equals(e.getActionCommand())){
                    blockClicked(board[6][6]);
                }else if("67".equals(e.getActionCommand())){
                    blockClicked(board[7][6]);
                }else if("70".equals(e.getActionCommand())){
                    blockClicked(board[0][7]);
                }else if("71".equals(e.getActionCommand())){
                    blockClicked(board[1][7]);
                }else if("72".equals(e.getActionCommand())){
                    blockClicked(board[2][7]);
                }else if("73".equals(e.getActionCommand())){
                    blockClicked(board[3][7]);
                }else if("74".equals(e.getActionCommand())){
                    blockClicked(board[4][7]);
                }else if("75".equals(e.getActionCommand())){
                    blockClicked(board[5][7]);
                }else if("76".equals(e.getActionCommand())){
                    blockClicked(board[6][7]);
                }else if("77".equals(e.getActionCommand())){
                    blockClicked(board[7][7]);
                }
            }else if (player == 2){
                
                if("77".equals(e.getActionCommand())){
                    blockClicked(board[0][0]);
                }else if("76".equals(e.getActionCommand())){
                    blockClicked(board[1][0]);
                }else if("75".equals(e.getActionCommand())){
                    blockClicked(board[2][0]);
                }else if("74".equals(e.getActionCommand())){
                    blockClicked(board[3][0]);
                }else if("73".equals(e.getActionCommand())){
                    blockClicked(board[4][0]);
                }else if("72".equals(e.getActionCommand())){
                    blockClicked(board[5][0]);
                }else if("71".equals(e.getActionCommand())){
                    blockClicked(board[6][0]);
                }else if("70".equals(e.getActionCommand())){
                    blockClicked(board[7][0]);
                }else if("67".equals(e.getActionCommand())){
                    blockClicked(board[0][1]);
                }else if("66".equals(e.getActionCommand())){
                    blockClicked(board[1][1]);
                }else if("65".equals(e.getActionCommand())){
                    blockClicked(board[2][1]);
                }else if("64".equals(e.getActionCommand())){
                    blockClicked(board[3][1]);
                }else if("63".equals(e.getActionCommand())){
                    blockClicked(board[4][1]);
                }else if("62".equals(e.getActionCommand())){
                    blockClicked(board[5][1]);
                }else if("61".equals(e.getActionCommand())){
                    blockClicked(board[6][1]);
                }else if("60".equals(e.getActionCommand())){
                    blockClicked(board[7][1]);
                }else if("57".equals(e.getActionCommand())){
                    blockClicked(board[0][2]);
                }else if("56".equals(e.getActionCommand())){
                    blockClicked(board[1][2]);
                }else if("55".equals(e.getActionCommand())){
                    blockClicked(board[2][2]);
                }else if("54".equals(e.getActionCommand())){
                    blockClicked(board[3][2]);
                }else if("53".equals(e.getActionCommand())){
                    blockClicked(board[4][2]);
                }else if("52".equals(e.getActionCommand())){
                    blockClicked(board[5][2]);
                }else if("51".equals(e.getActionCommand())){
                    blockClicked(board[6][2]);
                }else if("50".equals(e.getActionCommand())){
                    blockClicked(board[7][2]);
                }else if("47".equals(e.getActionCommand())){
                    blockClicked(board[0][3]);
                }else if("46".equals(e.getActionCommand())){
                    blockClicked(board[1][3]);
                }else if("45".equals(e.getActionCommand())){
                    blockClicked(board[2][3]);
                }else if("44".equals(e.getActionCommand())){
                    blockClicked(board[3][3]);
                }else if("43".equals(e.getActionCommand())){
                    blockClicked(board[4][3]);
                }else if("42".equals(e.getActionCommand())){
                    blockClicked(board[5][3]);
                }else if("41".equals(e.getActionCommand())){
                    blockClicked(board[6][3]);
                }else if("40".equals(e.getActionCommand())){
                    blockClicked(board[7][3]);
                }else if("37".equals(e.getActionCommand())){
                    blockClicked(board[0][4]);
                }else if("36".equals(e.getActionCommand())){
                    blockClicked(board[1][4]);
                }else if("35".equals(e.getActionCommand())){
                    blockClicked(board[2][4]);
                }else if("34".equals(e.getActionCommand())){
                    blockClicked(board[3][4]);
                }else if("33".equals(e.getActionCommand())){
                    blockClicked(board[4][4]);
                }else if("32".equals(e.getActionCommand())){
                    blockClicked(board[5][4]);
                }else if("31".equals(e.getActionCommand())){
                    blockClicked(board[6][4]);
                }else if("30".equals(e.getActionCommand())){
                    blockClicked(board[7][4]);
                }else if("27".equals(e.getActionCommand())){
                    blockClicked(board[0][5]);
                }else if("26".equals(e.getActionCommand())){
                    blockClicked(board[1][5]);
                }else if("25".equals(e.getActionCommand())){
                    blockClicked(board[2][5]);
                }else if("24".equals(e.getActionCommand())){
                    blockClicked(board[3][5]);
                }else if("23".equals(e.getActionCommand())){
                    blockClicked(board[4][5]);
                }else if("22".equals(e.getActionCommand())){
                    blockClicked(board[5][5]);
                }else if("21".equals(e.getActionCommand())){
                    blockClicked(board[6][5]);
                }else if("20".equals(e.getActionCommand())){
                    blockClicked(board[7][5]);
                }else if("17".equals(e.getActionCommand())){
                    blockClicked(board[0][6]);
                }else if("16".equals(e.getActionCommand())){
                    blockClicked(board[1][6]);
                }else if("15".equals(e.getActionCommand())){
                    blockClicked(board[2][6]);
                }else if("14".equals(e.getActionCommand())){
                    blockClicked(board[3][6]);
                }else if("13".equals(e.getActionCommand())){
                    blockClicked(board[4][6]);
                }else if("12".equals(e.getActionCommand())){
                    blockClicked(board[5][6]);
                }else if("11".equals(e.getActionCommand())){
                    blockClicked(board[6][6]);
                }else if("10".equals(e.getActionCommand())){
                    blockClicked(board[7][6]);
                }else if("07".equals(e.getActionCommand())){
                    blockClicked(board[0][7]);
                }else if("06".equals(e.getActionCommand())){
                    blockClicked(board[1][7]);
                }else if("05".equals(e.getActionCommand())){
                    blockClicked(board[2][7]);
                }else if("04".equals(e.getActionCommand())){
                    blockClicked(board[3][7]);
                }else if("03".equals(e.getActionCommand())){
                    blockClicked(board[4][7]);
                }else if("02".equals(e.getActionCommand())){
                    blockClicked(board[5][7]);
                }else if("01".equals(e.getActionCommand())){
                    blockClicked(board[6][7]);
                }else if("00".equals(e.getActionCommand())){
                    blockClicked(board[7][7]);
                }
            }
        }
    }
}