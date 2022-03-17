% Base case, empty list, do nothing
printList([]).
printList([H]) :- writeln(H).
printList([H | T]) :- writeln(H), printList(T).

% Check if one list is a subset of another
% https://stackoverflow.com/questions/26498507/determine-whether-a-list-is-sublist-of-another-list-in-prolog
subset([], _).
subset([First|Rest], List):-
  memberchk(First, List),
  subset(Rest, List).

% Checks if one room is reachable from another and tracks the path
% between the two of them
% Note: this does not include ToRoom in the path, (Would there be a way to do that?)
reachable(_, Room, Room, []).
reachable(Castle, FromRoom, ToRoom, [FromRoom | PathNextTo]) :-
  room(Castle, FromRoom, NextRoom, _),
  reachable(Castle, NextRoom, ToRoom, PathNextTo).

solveRooms(_, []).
solveRooms(Castle, Rooms) :-
  reachable(Castle, enter, exit, Path), % Possible Solutions
  append(Path, [exit], NewPath), % Add exit to the end of the path because otherwise it will not be printed
  subset(Rooms, NewPath), % Provided Rooms are a subset of solutions
  printList(NewPath).


solveRoomsWithinCost(Castle, Limit) :-
  reachable(Castle, enter, exit, Path), % Possible Solutions
  append(Path, [exit], NewPath), % Add exit to the end of the path because otherwise we don't include the final cost
  pathCost(Castle, NewPath, Cost),
  Cost =< Limit,
  write('Cost is '), write(Cost),
  write(' within limit of '), writeln(Limit),
  printList(NewPath).


pathCost(_, [], 0).
pathCost(_, [_], 0).
pathCost(Castle, [First, Next | Rest], Cost) :-
  room(Castle, First, Next, CostOne),
  pathCost(Castle, [Next | Rest], CostRest),
  Cost is CostOne + CostRest.
