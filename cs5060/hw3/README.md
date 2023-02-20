**Note**: This solution uses the following dependencies
```bash
$ pip install numpy matplotlib fitter
```

# Part 1

Run with
```bash
$ python Collings_Sean_HW3.py p1
```

Option Block Price (100): 902.368

# Part 2
Run with
```bash
$ python Collings_Sean_HW3.py p2
```
## Best Fit

Both stocks "best fit" was determined via the sum of the errors squared of the distributions attempted. The following distributions were tried:
```
[‘cauchy’, ‘chi2’, ‘expon’, ‘exponpow’, ‘gamma’, ‘lognorm’, ‘norm’, ‘powerlaw’, ‘rayleigh’, ‘uniform’, 'f']
```

The first stock was best fit by an f-distribution with the following parameters:
```
{'dfn': 4688.5278764807545, 'dfd': 36772.72953019279, 'loc': -130.2910677170893, 'scale': 230.19853590596097}
```

The second stock was best fit by chi squared distribution with the following parameters
```
{'df': 163.1459572174378, 'loc': 47.80042562252902, 'scale': 0.3374636068274328}
```

## Outperform Average
To attempt to outperform the provided stocks, the same process was taken as in part 1, but the avereage of each stock was used as the strike price, instead of the initial price.

= **Stock 1**:  910.866. This is extremely similar to the value seen in part one. I believe this is because the f-distributions and beta-distributions are related distributions, so they end up following similar tragectories.
- **Stock 2**: 614.730. This is substantially lower than the value seen in part one. I believe this is due to the chi^2 distribution performing better on average than the beta distribution, so the stock price is less likely to rise above it's average, thus lowering the value of the option.

## Outperform Max
The same process used used as above, but with the maximums instead of the averages. For both of the stocks, the resulting Option price was 0. I believe this to be because the maximum value that either of these stocks can take is higher than the beta-distrubtion modelled stock can take within a year time frame.
