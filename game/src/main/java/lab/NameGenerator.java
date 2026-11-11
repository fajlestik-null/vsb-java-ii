package lab;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class NameGenerator {
    public static final Random RANDOM = new Random();

    protected static final String[] nicks = { "CyberSurfer", "PixelPioneer", "SocialSavvy", "DigitalDynamo", "ByteBuddy", "InstaGuru",
        "TikTokTornado", "SnapMaster", "TweetTrendsetter", "ChatChampion", "HashtagHero", "EmojiEnthusiast",
        "StoryStylist", "SelfieStar", "FilterFanatic", "VlogVirtuoso", "Memelord", "InfluencerInsider",
        "StreamSupreme", "GeekyGizmo", "CodeCommander", "JavaJuggernaut", "ByteNinja", "SyntaxSamurai",
        "ClassyCoder", "ObjectOmnipotent", "LoopLegend", "VariableVirtuoso", "DebugDemon", "CompilerCrusader",
        "PixelProdigy", "VirtualVoyager", "AlgorithmAce", "DataDynamo", "ExceptionExpert", "BugBuster",
        "SyntaxSorcerer", "CodeCrusader", "JavaJester", "NerdyNavigator", "CryptoCaptain", "SocialButterfly",
        "AppArchitect", "WebWizard", "FunctionFreak", "PixelArtist", "CyberPhantom", "HackHero", "CacheChampion",
        "ScreenSage", "WebWeaver", "LogicLover", "BitBlazer", "NetworkNomad", "ProtocolPioneer", "BinaryBoss",
        "StackSultan", "SocialScribe", "RenderRuler", "ScriptSorcerer", "HTMLHero", "PixelProwler", "FrameFreak",
        "DataDreamer", "BotBuilder", "ByteBishop", "KeyboardKnight", "DesignDaredevil", "JavaJuggler",
        "SyntaxStrategist", "TechTactician", "ProgramProdigy", "BinaryBard", "PixelPoet", "GigabyteGuru",
        "TechTrekker", "NetworkNinja", "DataDetective", "MatrixMaster", "CodeConductor", "AppAlchemist",
        "ServerSage", "ClusterChampion", "ScriptSensei", "KeyboardKicker", "CacheCrafter", "SocialSpark",
        "BinaryBeast", "CodeConnoisseur", "BitBrain", "VirtualVanguard", "SystemSculptor", "RenderRogue",
        "CryptoConqueror", "MachineMonarch", "PixelPal", "CompilerCaptain", "BitBuilder", "TechTitan",
        "CloudConqueror", "EchoExplorer", "FunctionFanatic", "RobotRanger" };
    public static String getRandomNick() {
        return nicks[RANDOM.nextInt(nicks.length)];
    }
}

