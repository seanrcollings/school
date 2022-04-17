import Agents
import InteractiveDynamics
import GLMakie

# Required to have "id" and "pos" attributes
mutable struct Cell <: Agents.AbstractAgent
    id::Int
    pos::Tuple{Int, Int}
    status::Bool
end

function create_model()
    # Create the grid world and environment
    grid_size = (50, 50)
    grid = Agents.GridSpace(grid_size)
    model = Agents.ABM(Cell, grid)

    # Fill every grid with a Cell
    cell_id = 1
    for x in 1:grid_size[1], y in 1:grid_size[2]
        cell = Cell(cell_id, (x, y), false)
        Agents.add_agent_pos!(cell, model)
        cell_id += 1
    end

    return model
end

function step!(model::Agents.ABM)
    new_status = fill(false, Agents.nagents(model))
    for k in keys(model.agents)
        agent = model[k]
        n = sum(map((a) -> a.status, Agents.nearby_agents(agent, model)))

        if agent.status
            if  (n <  2 || n > 3)
                new_status[agent.id]  = false
            else
                new_status[agent.id] = true
            end
        elseif !agent.status && (n == 3)
            new_status[agent.id]= true
        end
    end

    for k in keys(model.agents)
        model[k].status = new_status[k]
    end
end

function initialize_cells(model)
    # Block
    model.agents[980].status = true
    model.agents[979].status = true
    model.agents[930].status = true
    model.agents[929].status = true

    # Bee Hive
    model.agents[760].status = true
    model.agents[758].status = true
    model.agents[710].status = true
    model.agents[708].status = true
    model.agents[809].status = true
    model.agents[659].status = true

    # Blinker
    model.agents[58].status = true
    model.agents[59].status = true
    model.agents[60].status = true

    # Penta-Decathlon
    model.agents[1481].status = true
    model.agents[1531].status = true
    model.agents[1581].status = true
    model.agents[1530].status = true
    model.agents[1529].status = true
    model.agents[1528].status = true
    model.agents[1478].status = true
    model.agents[1578].status = true

    model.agents[1476].status = true
    model.agents[1475].status = true
    model.agents[1525].status = true
    model.agents[1526].status = true
    model.agents[1575].status = true
    model.agents[1576].status = true

    model.agents[1473].status = true
    model.agents[1523].status = true
    model.agents[1573].status = true
    model.agents[1522].status = true
    model.agents[1521].status = true
    model.agents[1520].status = true
    model.agents[1470].status = true
    model.agents[1570].status = true

    # Glider
    model.agents[295].status = true
    model.agents[344].status = true
    model.agents[243].status = true
    model.agents[293].status = true
    model.agents[343].status = true

    # Space Ship
    model.agents[1795].status = true
    model.agents[1794].status = true
    model.agents[1793].status = true
    model.agents[1846].status = true
    model.agents[1843].status = true
    model.agents[1893].status = true
    model.agents[1943].status = true
    model.agents[1994].status = true
    model.agents[1996].status = true
end


################################################################################
# Main
################################################################################

model = create_model()
initialize_cells(model)

# Animate the model
alive(x::Cell) = x.status == true
dead(x::Cell) = x.status == false
color(x::Cell) = x.status ? "black" : "white"
as = 8
am = '●'
adata = [(alive, count), (dead, count)]
scatterkwargs = (strokewidth = 1.0,)

fig, p = InteractiveDynamics.abmexploration(
    model;
    model_step! = step!,
    ac = color,
    as,
    am,
    scatterkwargs,
    adata,
    alabels=["Alive", "Dead"],
)
fig