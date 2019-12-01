module AdventOfCode1Spec where

    import Test.Hspec
    
    parseDay1Input :: String -> IO [Integer]
    parseDay1Input file = do
        lines <- parseLines file
        return $ fmap parseLine lines
    
    parseLines :: String -> IO [String] 
    parseLines file = lines <$> (readFile file)
    
    parseLine  :: String -> Integer
    parseLine ('+':string) = read string
    parseLine            s = read s

    fuelRequirement :: Integer -> Integer
    fuelRequirement i = let result = (i `div`  3) - 2
                        in if   result <= 0 
                           then 0 
                           else result

    recursiveFuelRequirement :: Integer -> Integer
    recursiveFuelRequirement i = let fr = (fuelRequirement i) 
                                 in if   fr > 0 
                                    then fr + recursiveFuelRequirement fr 
                                    else fr

    recursiveTotalFuelRequirement :: [Integer] -> Integer
    recursiveTotalFuelRequirement i = sum $ map (\a -> recursiveFuelRequirement a) i

    totalFuelRequierement :: [Integer] -> Integer
    totalFuelRequierement m = sum $ (map (\i -> fuelRequirement i)) m

    spec :: Spec
    spec = describe "The Tyranny of the Rocket Equation" $ do
        it "gives the right fuel requierement" $ do
            fuelRequirement 12 `shouldBe` 2
            fuelRequirement 1969 `shouldBe` 654
            fuelRequirement 100756 `shouldBe` 33583

        it "gives test pattern matching" $ do
            recursiveFuelRequirement 1969 `shouldBe` 966
            recursiveFuelRequirement 100756 `shouldBe` 50346

        it "should read one line" $ do
            fmap head (parseDay1Input "day1-input.txt") `shouldReturn` 132709 

        it "should read 5th line" $ do
            fmap (head . drop 4) (parseDay1Input "day1-input.txt") `shouldReturn` 77219 

        it "is able to get the sum" $ do
            fmap totalFuelRequierement (parseDay1Input "day1-input.txt") `shouldReturn` 3393938 
    
        it "is able to get the sum of the total fuel" $ do 
            fmap recursiveTotalFuelRequirement (parseDay1Input "day1-input.txt") `shouldReturn` 5088037 
            