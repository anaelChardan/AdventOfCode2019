module AdventOfCode3Spec where

    import Test.Hspec
    
    data Vector = R Int | U Int | L Int | D Int deriving (Eq, Show)
    
    data Position = Position { x::Int, y::Int, stepsTo::Int } deriving (Eq, Show)
    
    get_all_positions :: Position -> Vector -> [Position]
    get_all_positions (Position x y s) (U length) = reverse $ map (\i -> Position x i 0) [ y  .. y + length] 
    get_all_positions (Position x y s) (D length) = map (\i -> Position x i 0) [ y - length .. y ] 
    get_all_positions (Position x y s) (R length) = reverse $ map (\i -> Position i y 0) [ x  .. x + length] 
    get_all_positions (Position x y s) (L length) = map (\i -> Position i y 0) [ x - length .. x] 
    
    accumulate_pos :: [Position] -> Vector -> [Position]
    accumulate_pos acc@(initpos : rest) vector = (get_all_positions initpos vector) ++ rest
    
    get_vectors_positions :: Position -> [Vector] -> [Position]
    get_vectors_positions initialPos [] = [] 
    get_vectors_positions initialPos vectors = reverse $ foldl accumulate_pos [initialPos] vectors
    
    closest_crosswire_distance :: [ Vector ] ->  [ Vector ] -> Integer
    closest_crosswire_distance = undefined


    absolute_difference :: Int -> Int -> Int
    absolute_difference a b = (max a b) - (min a b)

    manhattan_distance :: Position -> Position -> Int
    manhattan_distance a b = absolute_difference (x a) (x b) + absolute_difference (y a) (y b)
    
    spec :: Spec
    spec = describe "Crossed Wires" $ do
    
        -- it "first acceptance test" $ do
        --   closest_crosswire_distance [ R 75,D 30,R 83,U 83,L 12,D 49,R 71,U 7,L 72 ] [ U 62,R 66,U 55,R 34,D 71,R 55,D 58,R 83 ]
        --      `shouldBe` 159
    
        it "should return all position from a vector up" $ do
            get_all_positions (Position 0 0 0) (U 3) `shouldBe` reverse [Position 0 0 0, Position 0 1 0, Position 0 2 0, Position 0 3 0]
    
        it "should return all position from a vector down" $ do
            get_all_positions (Position 0 3 0) (D 3) `shouldBe` reverse [Position 0 3 0, Position 0 2 0, Position 0 1 0, Position 0 0 0]
    
        it "should return all position from a vector left" $ do
            get_all_positions (Position 3 0 0) (L 3) `shouldBe` reverse [Position 3 0 0, Position 2 0 0, Position 1 0 0, Position 0 0 0]
    
        it "should return all position from a vector right" $ do
            get_all_positions (Position 0 0 0) (R 3) `shouldBe` reverse [Position 0 0 0, Position 1 0 0, Position 2 0 0, Position 3 0 0]
    
        it "should return all positions from 2 vectors" $ do 
            get_vectors_positions (Position 0 0 0) [(U 3), (R 2)] `shouldBe` 
                [Position 0 0 0, Position 0 1 0, Position 0 2 0, Position 0 3 0, Position 1 3 0, Position 2 3 0]
    
        it "should return no positions from no vectors" $ do 
            get_vectors_positions (Position 0 0 0) [] `shouldBe` []

        it "does the absolute difference" $ do
            absolute_difference (-10) 10 `shouldBe` 20

        it "does manhattan distance" $ do
            manhattan_distance (Position 1 1 0) (Position 4 4 0) `shouldBe` 6