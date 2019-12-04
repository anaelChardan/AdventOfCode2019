module AdventOfCode2Spec where

    import Test.Hspec
    import Data.List.Split
    
    stringToInteger :: [String] -> [Integer]
    stringToInteger list = map (\s -> read s) list

    

    spec :: Spec
    spec = describe "The 1202 Program Alarm" $ do
        it "should read one line" $ do
            (head . stringToInteger) <$> (splitOn "," . head) <$> lines <$> readFile "day2-input.txt" `shouldReturn` 1
