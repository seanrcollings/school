% mother(sean, heidi).
% father(sean, greg).

% parent(X, Y) :- mother(X, Y) ; father(X, Y).

% ancestor(X, Y) :-
%     parent(X, Y), .
%     ancestor(X, Y)

len([], 0).
len([_|T], N) :-
    len(T, N1),
    N is N1 + 1.

