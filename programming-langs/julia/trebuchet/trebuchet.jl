## Imports
using Flux
using Blink
using Trebuchet
using Random

using Zygote: forwarddiff
using Statistics: mean


## Linear scaling
linear_interp(x, low, hi) = x * (hi - low) + low


## Shoot the trebuchet and return where the ball lands
function shoot(wind, angle, weight)
    state = Trebuchet.shoot(
        (wind, Trebuchet.deg2rad(angle), weight)
    )

    return state[2]
end


## Simulate the trebuchet using differential equations
shoot(parameters) = forwarddiff(parameter -> shoot(parameter...), parameters)


## Simple Nueral Network
##   2 inputs (wind, target)
##   2 outputs (angle, weight)
neural_net = f64(Chain(
    Dense(2, 16, sigmoid),
    Dense(16, 64, sigmoid),
    Dense(64, 16, sigmoid),
    Dense(16, 2)
))

## Ask the Neural Network to predict the angle and weight
## given the wind speed and target location
function aim(wind, target)::Tuple{Float64, Float64}
    angle, weight = neural_net([wind, target])
    angle = sigmoid(angle) * 90
    weight = weight + 200

    return angle, weight
end

## Helper function to combine the "aim" function with the "shoot" function
## Returns how far the ball lands
distance(wind, target) = shoot(collect([wind, aim(wind, target)...]))


## Error function - how far off are we?
loss(wind, target) = ((distance(wind, target) - target) ^ 2)


## Parameters for creating random trebuchet examples
DIST = (20, 100)  # Maximum target distance
SPEED = 5  # Maximum wind speed


## Generate a random trebuchet target and dataset of 20_000 examples
target() = (randn() * SPEED, linear_interp(rand(), DIST...))
dataset = (target() for i = 1:20_000)


## Logging ---
##   Average the error over 100 random examples
##   Visualize the trebuchet
meanloss() = mean(sqrt(loss(target()...)) for i = 1:100)
function log()
    println("Average error: ", meanloss())

    wind, target = (randn() * SPEED, linear_interp(rand(), DIST...))
    angle, weight = aim(wind, target)
    state = Trebuchet.shoot(
        (wind, Trebuchet.deg2rad(angle), weight)
    )[1]

    vis = visualise(state, target)
    body!(Blink.Window(), vis)
end


## Train the neural network
Flux.train!(
    loss,
    params(neural_net),
    dataset,
    ADAM(),
    cb=Flux.throttle( () -> log(), 10 )  # Log every 10 iterations
)Blink.Window()
